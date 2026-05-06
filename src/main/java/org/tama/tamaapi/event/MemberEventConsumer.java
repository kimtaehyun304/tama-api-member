package org.tama.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.command.CouponService;
import org.tama.tamaapi.command.DiscountLogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEventConsumer {

    private final String MEMBER_TOPIC = "member_topic";
    private final CouponService couponService;
    private final DiscountLogRepository discountLogRepository;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 3000, multiplier = 2)
    )
    @KafkaListener(topics = MEMBER_TOPIC, groupId = "member_consumer_group")
    public void consumeRollbackCouponAndPointEvent(RollbackCouponAndPointEvent event, Acknowledgment ack) {
        //재고 차감 → 쿠폰 적용 → 주문 저장 순이라, order 저장 전이라 order 조회 불가하여 zero payload 불가
        //할인 적용 안 됐는데 타임아웃만 난 경우도 있어서 체크헤야함
        if(discountLogRepository.existsByPaymentId(event.getPaymentId()))
            couponService.rollbackDiscountAndDeleteLog(event.getMemberCouponId(), event.getUsedPoint(), event.getRewardPoint(), event.getMemberId(), event.getPaymentId());
        ack.acknowledge();
    }

}