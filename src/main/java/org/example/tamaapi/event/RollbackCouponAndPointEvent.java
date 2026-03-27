package org.example.tamaapi.event;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RollbackCouponAndPointEvent {

    private final EventType eventType = EventType.ROLLBACK_COUPON_AND_POINT;
    private String paymentId;
    private Long memberCouponId;
    private Integer usedPoint;
    private Integer rewardPoint;
    private Long memberId;
}
