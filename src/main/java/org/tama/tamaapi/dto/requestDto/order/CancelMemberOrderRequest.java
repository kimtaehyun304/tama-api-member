package org.tama.tamaapi.dto.requestDto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//로그 남기는 로직있어서 @ToString 사용
public class CancelMemberOrderRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Boolean isFreeOrder;

}
