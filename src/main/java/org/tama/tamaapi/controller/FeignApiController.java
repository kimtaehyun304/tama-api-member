package org.tama.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.sharelib.common.aspect.InternalOnly;
import org.tama.tamaapi.command.CouponService;
import org.tama.tamaapi.exception.ErrorMessageUtil;
import org.tama.tamaapi.domain.user.Authority;
import org.tama.tamaapi.domain.user.Member;
import org.tama.tamaapi.dto.feign.MemberResponse;
import org.tama.tamaapi.dto.feign.UsedCouponAndPointRequest;

import org.tama.tamaapi.query.CouponQueryService;
import org.tama.tamaapi.query.DiscountLogQueryRepository;
import org.tama.tamaapi.query.MemberQueryRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@InternalOnly
@Slf4j
public class FeignApiController {


    private final CouponQueryService couponQueryService;
    private final CouponService couponService;
    private final MemberQueryRepository memberQueryRepository;
    private final DiscountLogQueryRepository discountLogQueryRepository;


    @GetMapping("/api/member/coupon/{memberCouponId}/price")
    public int getCouponPrice(@PathVariable Long memberCouponId, int orderItemsPrice){
        return couponQueryService.getCouponPrice(memberCouponId, orderItemsPrice);
    }

    @PutMapping("/api/member/discount/use")
    public void useCouponAndPoint(@RequestBody UsedCouponAndPointRequest usedCouponAndPointRequest) throws InterruptedException {
        log.info("쿠폰 차감 컨트롤러 시작");
        couponService.useCouponAndPoint(usedCouponAndPointRequest);
    }

    /*  관리자가 주문 취소할 때 필요 (아직 기능 미구현이라)
    @PutMapping("/api/member/discount/rollback")
    public void rollbackCouponAndPoint(@RequestBody UsedCouponAndPointRequest usedCouponAndPointRequest, @AuthenticationPrincipal Long memberId){
        couponService.rollbackCouponAndPoint(usedCouponAndPointRequest, memberId);
    }
     */

    @GetMapping("/api/member/{memberId}/authority")
    public Authority findAuthority(@PathVariable Long memberId){
        return memberQueryRepository.findAuthorityById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));
    }

    //---읽기 msa 동기화---
    //kafka payload에 jwt 심으면 부하 있을 것 같아서 memberId 사용
    @GetMapping("/api/member/{memberId}")
    public MemberResponse findMember(@PathVariable Long memberId){
        Member member = memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));
        return new MemberResponse(member);
    }

    @GetMapping("/api/member/discount/log/exist")
    boolean existDisCountLog(@RequestParam String paymentId){
        return discountLogQueryRepository.existsByPaymentId(paymentId);
    }

}
