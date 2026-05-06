package org.tama.tamaapi.feignClient.item;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.tamaapi.dto.responseDto.ItemOrderCountResponse;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemTotalPriceRequest {

    @NotNull
    private Long colorItemSizeStockId;

    @NotNull
    private Integer orderCount;

    public ItemTotalPriceRequest(ItemOrderCountResponse response) {
        this.colorItemSizeStockId = response.colorItemSizeStockId();
        this.orderCount = response.orderCount();
    }
}
