package org.example.tamaapi.controller;

import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
<<<<<<< HEAD
import org.example.tamaapi.command.CouponService;
import org.example.tamaapi.common.aspect.InternalOnly;
import org.example.tamaapi.common.auth.CustomPrincipal;
import org.example.tamaapi.common.util.ErrorMessageUtil;
import org.example.tamaapi.domain.user.Authority;
import org.example.tamaapi.domain.user.Member;
import org.example.tamaapi.dto.feign.UsedCouponAndPointRequest;
import org.example.tamaapi.feignClient.member.MemberResponse;
import org.example.tamaapi.query.CouponQueryService;
import org.example.tamaapi.query.MemberQueryRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

=======
=======
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
import org.example.tamaapi.command.item.ItemService;
import org.example.tamaapi.common.aspect.InternalOnly;
import org.example.tamaapi.common.util.ErrorMessageUtil;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemImage;
import org.example.tamaapi.domain.item.ColorItemSizeStock;
import org.example.tamaapi.domain.item.Item;
import org.example.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;
import org.example.tamaapi.dto.feign.requestDto.ItemOrderCountRequestWrapper;
import org.example.tamaapi.dto.feign.responseDto.*;
import org.example.tamaapi.query.item.ColorItemImageQueryRepository;
import org.example.tamaapi.query.item.ColorItemSizeStockQueryRepository;
import org.example.tamaapi.query.item.ItemQueryRepository;
import org.example.tamaapi.query.item.service.ItemQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
=======
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
@RestController
@RequiredArgsConstructor
@InternalOnly
public class FeignApiController {

<<<<<<< HEAD
<<<<<<< HEAD
    private final CouponQueryService couponQueryService;
    private final CouponService couponService;
    private final MemberQueryRepository memberQueryRepository;

    //쿠폰 소유한 멤버만 보는게 이상적이지만. 안해도 보안 위험 없을 것 같은데
    @GetMapping("/api/member/coupon/{memberCouponId}/price")
    public int getCouponPrice(@PathVariable Long memberCouponId, int orderItemsPrice){
        return couponQueryService.getCouponPrice(memberCouponId, orderItemsPrice);
    }

    @PutMapping("/api/member/discount/use")
    public void useCouponAndPoint(@RequestBody UsedCouponAndPointRequest usedCouponAndPointRequest, @AuthenticationPrincipal CustomPrincipal principal){
        couponService.useCouponAndPoint(usedCouponAndPointRequest, principal);
    }

    @PutMapping("/api/member/discount/rollback")
    public void rollbackCouponAndPoint(@RequestBody UsedCouponAndPointRequest usedCouponAndPointRequest, @AuthenticationPrincipal CustomPrincipal principal){
        couponService.rollbackCouponAndPoint(usedCouponAndPointRequest, principal);
    }

    @GetMapping("/api/member/authority")
    public Authority findAuthority(@AuthenticationPrincipal CustomPrincipal principal){
        return memberQueryRepository.findAuthorityById(principal.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));
    }

    //---읽기 msa 동기화---
    @GetMapping("/api/member/{memberId}")
    public MemberResponse findMember(@PathVariable Long memberId){
        Member member = memberQueryRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessageUtil.NOT_FOUND_MEMBER));
        return new MemberResponse(member);
=======
=======
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
    private final ItemQueryService itemQueryService;
    private final ItemService itemService;
    private final ItemQueryRepository itemQueryRepository;
    private final ColorItemSizeStockQueryRepository colorItemSizeStockQueryRepository;
    private final ColorItemImageQueryRepository colorItemImageQueryRepository;

    //-----주문 msa-----
    @GetMapping("/api/items/totalPrice")
    public int getTotalPrice(@RequestBody List<ItemOrderCountRequest> requests) {
        return itemQueryService.getItemsTotalPrice(requests);
        //return itemQueryService.getItemsTotalPrice(wrapper.getItemOrderCountRequests());
    }

    //주문 아이템 생성시 필요
    @GetMapping("/api/items/price")
    public List<ItemPriceFeignResponse> getItemsPrice(@RequestParam List<Long> colorItemSizeStockIds) {
        return itemQueryService.getItemsPrice(colorItemSizeStockIds);
    }

    @PutMapping("/api/items/stocks/increase")
    public void increaseStocks(@RequestBody List<ItemOrderCountRequest> requests) {
        itemService.increaseStocks(requests);
    }

    @PutMapping("/api/items/stocks/decrease")
    public void decreaseStocks(@RequestBody List<ItemOrderCountRequest> requests) {
        itemService.decreaseStocks(requests);
    }

    //-----읽기 msa-----
    @GetMapping("/api/items/{itemId}")
    public ItemSyncResponse getItem(@PathVariable Long itemId) {
        return itemQueryService.createItemSyncResponse(itemId);
<<<<<<< HEAD
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
=======
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
    }

}
