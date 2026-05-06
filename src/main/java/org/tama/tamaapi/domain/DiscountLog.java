package org.tama.tamaapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//row가 있으면 재고 차감이 된거라고 판단 (트랜잭션 묶어논거라)
public class DiscountLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_log_id")
    private Long id;

    private String paymentId;

    private Long memberId;

    private Long memberCouponId;

    private Integer usedPoint;

    private Integer rewardPoint;


    public DiscountLog(String paymentId, Long memberId, Long memberCouponId, Integer usedPoint, Integer rewardPoint) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.memberCouponId = memberCouponId;
        this.usedPoint = usedPoint;
        this.rewardPoint = rewardPoint;
    }
}