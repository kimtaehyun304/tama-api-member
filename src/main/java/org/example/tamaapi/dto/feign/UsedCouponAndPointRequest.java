package org.example.tamaapi.dto.feign;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsedCouponAndPointRequest {

    private String paymentId;

    private Long memberCouponId;

    private int usedCouponPrice;

    private int usedPoint;

    private int rewardPoint;

    private int orderItemsPrice;

    private Long memberId;

}
