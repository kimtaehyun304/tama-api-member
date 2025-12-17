package org.example.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemEventProducer {
    private final KafkaTemplate<String, ItemCreatedEvent> kafkaTemplate;
    private final String ITEM_TOPIC = "item_topic";

    /*
    public void produceOrderCreatedEvent(Long orderId, OrderStatus orderStatus){
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(orderId, orderStatus);
        kafkaTemplate.send(ORDER_TOPIC, orderCreatedEvent);
    }
     */

    @Async
    public void produceAsyncItemCreatedEvent(Long itemId){
        try {
            ItemCreatedEvent itemCreatedEvent = new ItemCreatedEvent(itemId);
            kafkaTemplate.send(ITEM_TOPIC, itemCreatedEvent);
        } catch (Exception e){
            log.error("카프카 발송 실패. 이유={}",e.getMessage());
        }
    }

}
