package org.example.tamaapi.feignClient.order;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class OrderFallbackFactory implements FallbackFactory<OrderFallback> {

    @Override
    public OrderFallback create(Throwable cause) {
        return new OrderFallback(cause);
    }

}
