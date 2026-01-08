package org.example.tamaapi.common.exception.feign;

public class OrderFeignCommandException extends RuntimeException{
    public OrderFeignCommandException(String message) {
        super(message);
    }
}
