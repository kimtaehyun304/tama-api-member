package org.example.tamaapi.dto.feign.responseDto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemImage;
import org.example.tamaapi.dto.UploadFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorItemImageResponse {

    private Long id;

    private Long colorItemId;

    private UploadFile uploadFile;

    private Integer sequence;

    public ColorItemImageResponse(ColorItemImage colorItemImage) {
        id = colorItemImage.getId();
        colorItemId = colorItemImage.getColorItem().getId();
        uploadFile = colorItemImage.getUploadFile();
        sequence = colorItemImage.getSequence();
    }

}
