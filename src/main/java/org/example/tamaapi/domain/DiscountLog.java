package org.example.tamaapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

    //private Long memberId;

    /*
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode payload;
     */

    public DiscountLog(String paymentId) {
        this.paymentId = paymentId;
    }

}