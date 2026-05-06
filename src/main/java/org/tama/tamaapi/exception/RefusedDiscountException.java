package org.tama.tamaapi.exception;


import org.tama.sharelib.common.exception.CustomFeignException;

public class RefusedDiscountException extends CustomFeignException {

    //검증 로직에서 실패하면 예외값 넣는 구조입니다. 이외 로직에서 인위적으로 넣지마세요
    public RefusedDiscountException(String message) {
        super("REFUSED_DISCOUNT", message);
    }
}
