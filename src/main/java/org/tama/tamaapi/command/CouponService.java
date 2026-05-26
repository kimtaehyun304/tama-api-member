package org.tama.tamaapi.command;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.exception.RefusedDiscountException;
import org.tama.tamaapi.domain.DiscountLog;
import org.tama.tamaapi.domain.user.Member;
import org.tama.tamaapi.domain.user.coupon.MemberCoupon;
import org.tama.tamaapi.dto.feign.UsedCouponAndPointRequest;
import org.tama.tamaapi.query.MemberQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.tama.tamaapi.exception.ErrorMessageUtil.NOT_FOUND_COUPON;
import static org.tama.tamaapi.exception.ErrorMessageUtil.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CouponService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final DiscountLogRepository discountLogRepository;
    private final EntityManager em;

    //+로그 테이블 저장
    public void useCouponAndPoint(UsedCouponAndPointRequest request) {
        Long memberCouponId = request.getMemberCouponId();
        int usedCouponPrice = request.getUsedCouponPrice();
        int usedPoint = request.getUsedPoint();
        int rewardPoint = request.getRewardPoint();
        int orderItemsPrice = request.getOrderItemsPrice();
        Long memberId = request.getMemberId();
        String paymentId = request.getPaymentId();

        validatePoint(usedPoint, memberId);
        MemberCoupon memberCoupon = null;

        //쿠폰 사용 처리
        if (memberCouponId != null) {
            memberCoupon = memberCouponRepository.findById(memberCouponId)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_COUPON));
            validateCoupon(memberCoupon, memberId, usedCouponPrice, orderItemsPrice);
            memberCoupon.changeIsUsed(true);
        }

        //포인트 로직 준비
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER));

        member.changePoint(rewardPoint - usedPoint);

        discountLogRepository.save(new DiscountLog(paymentId, memberId, memberCouponId, usedPoint, rewardPoint));
        log.info("쿠폰, 포인트 사용 및 포인트 적립 완료");
    }


    public void rollbackCouponAndPoint(Long memberCouponId, Integer usedPoint, Integer rewardPoint, Long memberId) {
        //사용한 쿠폰 롤백
        if (memberCouponId != null) {
            MemberCoupon memberCoupon = memberCouponRepository.findByIdAndMemberId(memberCouponId, memberId)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_COUPON));
            memberCoupon.changeIsUsed(false);
        }

        //포인트 로직 준비
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER));

        //사용한 포인트 원복, 적립 포인트 원복
        member.changePoint(usedPoint - rewardPoint);
        log.info("쿠폰, 포인트 사용 및 포인트 적립 롤백 완료");
    }


    //없는 데이터 조회하는경우 보안을 위해 일반 예외로해서 예외 메시지 감춤
    private void validatePoint(int usedPoint, Long memberId) {
        Member member = memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER));

        int serverPoint = member.getPoint();


        if (usedPoint > serverPoint)
            throw new RefusedDiscountException("보유한 포인트보다 넘게 사용할 수 없습니다");
    }

    private void validateCoupon(MemberCoupon memberCoupon, Long memberId, int couponPrice, int orderItemsPrice) {
        if (!memberCoupon.getMember().getId().equals(memberId))
            throw new RefusedDiscountException("보유하지 않은 쿠폰을 사용했습니다.");

        if (memberCoupon.getCoupon().getExpiresAt().isBefore(LocalDate.now()))
            throw new RefusedDiscountException("쿠폰 유효기간 만료");

        if (memberCoupon.isUsed())
            throw new RefusedDiscountException("이미 사용한 쿠폰입니다.");

        if (couponPrice > orderItemsPrice)
            throw new RefusedDiscountException("쿠폰 금액은 주문 가격보다 넘게 사용할 수 없습니다.");
    }

    public void deleteDiscountLog(String paymentId) {
        int deletedRow = em.createQuery("delete DiscountLog d where d.paymentId = :paymentId")
                .setParameter("paymentId", paymentId)
                .executeUpdate();

        if (deletedRow == 0)
            throw new IllegalArgumentException("로그 삭제 실패");
    }


    //트랜잭션 묶으려고 합침
    public void rollbackDiscountAndDeleteLog(Long memberCouponId, Integer usedPoint, Integer rewardPoint, Long memberId, String paymentId) {
        rollbackCouponAndPoint(memberCouponId, usedPoint, rewardPoint, memberId);
        deleteDiscountLog(paymentId);
        log.info("쿠폰 롤백 완료. paymentId = {}", paymentId);
    }

    public void deleteDiscountLogInPaymentIds(List<String> paymentIds) {
        //주문 없으면 비었을수도 있음
        if (paymentIds.isEmpty())
            return;

        int deletedRow = em.createQuery("delete DiscountLog d where d.paymentId in :paymentIds")
                .setParameter("paymentIds", paymentIds)
                .executeUpdate();

        if (deletedRow == 0)
            throw new IllegalArgumentException("로그 삭제 실패");
    }

}
