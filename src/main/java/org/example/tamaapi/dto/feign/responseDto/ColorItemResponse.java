package org.example.tamaapi.dto.feign.responseDto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tamaapi.domain.Gender;
import org.example.tamaapi.domain.item.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorItemResponse {

    private Long id;

    private Long itemId;

    private Long colorId;

    public ColorItemResponse(ColorItem colorItem) {
        id = colorItem.getId();
        itemId = colorItem.getItem().getId();
        colorId = colorItem.getColor().getId();
    }

}
