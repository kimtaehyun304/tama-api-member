package org.tama.tamaapi.dto.requestDto.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemRequestWrapper {

    private List<ItemOrderCountRequest> orderItems;

}
