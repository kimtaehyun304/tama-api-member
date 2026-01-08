package org.example.tamaapi.feignClient.order;


import java.util.List;

import static org.example.tamaapi.common.exception.CommonExceptionHandler.throwOriginalException;

public class OrderFallback implements OrderFeignClient {

    private final Throwable cause;

    public OrderFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public List<OrderItemFeignResponse> getOrderItems(Long orderId) {
        throwOriginalException(cause);
        return null;
    }

    @Override
    public Long getOrderItemMember(Long orderItemId, String jwt) {
        throwOriginalException(cause);
        return null;
    }
}
