package org.example.tamaapi.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.command.CouponService;
import org.example.tamaapi.command.DiscountLogRepository;
import org.example.tamaapi.command.MemberService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Async;
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
    //retry + ack 인데 왜 되지? order msa에선 안됐는데
    @Transactional
    public void consumeRollbackCouponAndPointEvent(RollbackCouponAndPointEvent event, Acknowledgment ack) {
        //재고 차감 → 쿠폰 적용 → 주문 저장 순이라, order 저장 전이라 order 조회 불가하여 zero payload 불가
        System.out.println("event = " + event);
        //할인 적용 안 됐는데 타임아웃만 난 경우도 있어서 체크헤야함
        if(discountLogRepository.existsByPaymentId(event.getPaymentId())) {
            couponService.rollbackCouponAndPoint(event.getMemberCouponId(), event.getUsedPoint(), event.getRewardPoint(), event.getMemberId());
            couponService.deleteDiscountLog(event.getPaymentId());
        }
        ack.acknowledge();
    }

}