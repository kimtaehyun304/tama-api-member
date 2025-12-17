package org.example.tamaapi.dto.feign.responseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tamaapi.domain.Gender;
import org.example.tamaapi.domain.item.Item;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSyncResponse {

    private ItemResponse item;

    private List<ColorItemResponse> colorItems;

    private List<ColorItemSizeStockResponse> colorItemSizeStocks;

    private List<ColorItemImageResponse> colorItemImages;

}
