package org.tama.tamaapi.feignClient.item;

import java.util.List;

import static org.tama.tamaapi.exception.CommonExceptionHandler.throwOriginalException;


public class ItemFallback implements ItemFeignClient{

    private final Throwable cause;

    public ItemFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public int getTotalPrice(List<ItemTotalPriceRequest> requests) {
        throwOriginalException(cause);
        return 0;
    }

    @Override
    public List<ItemPriceResponse> getItemsPrice(List<Long> colorItemSizeStockIds) {
        throwOriginalException(cause);
        return null;
    }

}
