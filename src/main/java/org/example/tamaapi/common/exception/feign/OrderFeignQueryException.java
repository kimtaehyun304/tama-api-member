package org.example.tamaapi.common.exception.feign;

public class OrderFeignQueryException extends RuntimeException{
    public OrderFeignQueryException(String message) {
        super(message);
    }
}
