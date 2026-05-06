package org.tama.tamaapi.event;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RollbackCouponAndPointEvent {

    private String paymentId;
    private Long memberCouponId;
    private Integer usedPoint;
    private Integer rewardPoint;
    private Long memberId;
}
