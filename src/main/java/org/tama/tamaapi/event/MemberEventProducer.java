package org.tama.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEventProducer {
    private final KafkaTemplate<String, MemberCreatedEvent> kafkaTemplate;
    private final String MEMBER_TOPIC = "member_topic";

    /*
    //send 자체가 비동기라서 @Async 써도 별로 차이 없음
    @Async
    public void produceAsyncMemberCreatedEvent(Long memberId){
        try {
            MemberCreatedEvent memberCreatedEvent = new MemberCreatedEvent(memberId);
            kafkaTemplate.send(MEMBER_TOPIC, memberCreatedEvent);
        } catch (Exception e){
            log.error("카프카 발송 실패. 이유={}",e.getMessage());
        }
    }
    */

    public void produceMemberCreatedEvent(Long memberId) {
        MemberCreatedEvent memberCreatedEvent = new MemberCreatedEvent(memberId);
        kafkaTemplate.send(MEMBER_TOPIC, memberCreatedEvent)
                .exceptionally(ex -> {
                    // 실패
                    log.error("Kafka 발송 실패 memberId={}", memberId, ex);
                    return null;
                });
    }
}