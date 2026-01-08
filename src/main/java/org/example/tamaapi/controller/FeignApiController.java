package org.example.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.tamaapi.command.CouponService;
import org.example.tamaapi.common.aspect.InternalOnly;
import org.example.tamaapi.common.util.ErrorMessageUtil;
import org.example.tamaapi.domain.user.Authority;
import org.example.tamaapi.domain.user.Member;
import org.example.tamaapi.dto.feign.MemberResponse;
import org.example.tamaapi.dto.feign.UsedCouponAndPointRequest;

import org.example.tamaapi.query.CouponQueryService;
import org.example.tamaapi.query.MemberQueryRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@InternalOnly
public class FeignApiController {


    private final CouponQueryService couponQueryService;
    private final CouponService couponService;
    private final MemberQueryRepository memberQueryRepository;

    //쿠폰 소유한 멤버만 보는게 이상적이지만. 안해도 보안 위험 없을 것 같은데
    @GetMapping("/api/member/coupon/{memberCouponId}/price")
    public int getCouponPrice(@PathVariable Long memberCouponId, int orderItemsPrice){
        return couponQueryService.getCouponPrice(memberCouponId, orderItemsPrice);
    }

    @PutMapping("/api/member/discount/use")
    public void useCouponAndPoint(@RequestBody UsedCouponAndPointRequest usedCouponAndPointRequest, @AuthenticationPrincipal Long memberId) throws InterruptedException {
        couponService.useCouponAndPoint(usedCouponAndPointRequest, memberId);
    }

    /*  관리자가 주문 취소할 때 필요 (아직 기능 미구현이라)
    @PutMapping("/api/member/discount/rollback")
    public void rollbackCouponAndPoint(@RequestBody UsedCouponAndPointRequest usedCouponAndPointRequest, @AuthenticationPrincipal Long memberId){
        couponService.rollbackCouponAndPoint(usedCouponAndPointRequest, memberId);
    }
     */

    @GetMapping("/api/member/authority")
    public Authority findAuthority(@AuthenticationPrincipal Long memberId){
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

}
