package org.tama.tamaapi.feignClient.item;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//k8s로 바꾸면서 url 옵션 생략
@FeignClient(name = "item-service"
        , configuration = ItemFeignClientConfig.class
        , fallbackFactory = ItemFallbackFactory.class)
public interface ItemFeignClient {

    @GetMapping("/api/items/totalPrice")
    int getTotalPrice(@RequestBody List<ItemTotalPriceRequest> requests);


    @GetMapping("/api/items/price")
    List<ItemPriceResponse> getItemsPrice(@RequestBody List<Long> colorItemSizeStockIds);
}
