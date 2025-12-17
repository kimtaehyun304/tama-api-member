package org.example.tamaapi.command;

import lombok.RequiredArgsConstructor;
import org.example.tamaapi.common.auth.CustomPrincipal;
import org.example.tamaapi.common.exception.OrderFailException;
import org.example.tamaapi.domain.user.Member;
import org.example.tamaapi.domain.user.coupon.MemberCoupon;
import org.example.tamaapi.dto.feign.UsedCouponAndPointRequest;
import org.example.tamaapi.feignClient.item.ItemFeignClient;
import org.example.tamaapi.feignClient.item.ItemTotalPriceRequest;
import org.example.tamaapi.feignClient.order.OrderFeignClient;
import org.example.tamaapi.feignClient.order.OrderResponse;
import org.example.tamaapi.feignClient.order.ItemOrderCountResponse;
import org.example.tamaapi.query.MemberQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.example.tamaapi.common.util.ErrorMessageUtil.NOT_FOUND_COUPON;
import static org.example.tamaapi.common.util.ErrorMessageUtil.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;

    public void useCouponAndPoint(UsedCouponAndPointRequest request, CustomPrincipal principal){
        Long memberCouponId = request.getMemberCouponId();
        int usedCouponPrice = request.getUsedCouponPrice();
        int usedPoint = request.getUsedPoint();
        int rewardPoint = request.getRewardPoint();
        int orderItemsPrice = request.getOrderItemsPrice();
        Long memberId = principal.getMemberId();
        validatePoint(usedPoint, memberId);
        MemberCoupon memberCoupon = null;

        //쿠폰 사용 처리
        if (memberCouponId != null) {
            memberCoupon = memberCouponRepository.findById(memberCouponId)
                    .orElseThrow(() -> new OrderFailException(NOT_FOUND_COUPON));
            validateCoupon(memberCoupon, memberId, usedCouponPrice, orderItemsPrice);
            memberCoupon.changeIsUsed(true);
        }

        //포인트 로직 준비
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new OrderFailException(NOT_FOUND_MEMBER));

        //사용한 포인트 차감
        member.minusPoint(usedPoint);

        //포인트 적립
        member.plusPoint(rewardPoint);
    }

    public void rollbackCouponAndPoint(UsedCouponAndPointRequest request, CustomPrincipal principal){
        Long memberCouponId = request.getMemberCouponId();
        //사용한 쿠폰 롤백
        if (memberCouponId != null) {
            MemberCoupon memberCoupon = memberCouponRepository.findByIdAndMemberId(memberCouponId, principal.getMemberId())
                    .orElseThrow(() -> new OrderFailException(NOT_FOUND_COUPON));
            memberCoupon.changeIsUsed(false);
        }

        int usedPoint = request.getUsedPoint();
        int rewardPoint = request.getRewardPoint();

        //포인트 로직 준비
        Member member = memberRepository.findById(principal.getMemberId())
                .orElseThrow(() -> new OrderFailException(NOT_FOUND_MEMBER));

        //사용한 포인트 롤백
        member.plusPoint(usedPoint);

        //적립한 포인트 롤백
        member.minusPoint(rewardPoint);
    }

    private void validatePoint(int usedPoint, Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new OrderFailException(NOT_FOUND_MEMBER));

        int serverPoint = member.getPoint();

        if (usedPoint > serverPoint)
            throw new OrderFailException("보유한 포인트보다 넘게 사용할 수 없습니다");
    }

    private void validateCoupon(MemberCoupon memberCoupon, Long memberId, int couponPrice, int orderItemsPrice) {
        if(!memberCoupon.getMember().getId().equals(memberId))
            throw new OrderFailException("보유하지 안은 쿠폰을 사용했습니다.");

        if (memberCoupon.getCoupon().getExpiresAt().isBefore(LocalDate.now()))
            throw new OrderFailException("쿠폰 유효기간 만료");

        if(memberCoupon.isUsed())
            throw new OrderFailException("이미 사용한 쿠폰입니다.");

        if(couponPrice > orderItemsPrice)
            throw new OrderFailException("쿠폰 금액은 주문 가격보다 넘게 사용할 수 없습니다.");
    }

}
