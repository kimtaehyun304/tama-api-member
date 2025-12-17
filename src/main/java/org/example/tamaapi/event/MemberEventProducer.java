package org.example.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEventProducer {
    private final KafkaTemplate<String, MemberCreatedEvent> kafkaTemplate;
    private final String MEMBER_TOPIC = "member_topic";

    @Async
    public void produceAsyncMemberCreatedEvent(Long memberId){
        try {
            MemberCreatedEvent memberCreatedEvent = new MemberCreatedEvent(memberId);
            kafkaTemplate.send(MEMBER_TOPIC, memberCreatedEvent);
        } catch (Exception e){
            log.error("카프카 발송 실패. 이유={}",e.getMessage());
        }
    }

}