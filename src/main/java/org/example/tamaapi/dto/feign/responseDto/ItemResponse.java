package org.example.tamaapi.dto.feign.responseDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tamaapi.domain.Gender;
import org.example.tamaapi.domain.item.Category;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.Item;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemResponse {

    private Long id;

    private Integer originalPrice;

    private Integer nowPrice;

    private Gender gender;

    private String yearSeason;

    private String name;

    private String description;

    private LocalDate dateOfManufacture;

    private String countryOfManufacture;

    private String manufacturer;

    private Long categoryId;

    private String textile;

    private String precaution;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.originalPrice = item.getOriginalPrice();
        this.nowPrice = item.getNowPrice();
        this.gender = item.getGender();
        this.yearSeason = item.getYearSeason();
        this.name = item.getName();
        this.description = item.getDescription();
        this.dateOfManufacture = item.getDateOfManufacture();
        this.countryOfManufacture = item.getCountryOfManufacture();
        this.manufacturer = item.getManufacturer();
        this.categoryId = item.getCategory().getId();
        this.textile = item.getTextile();
        this.precaution = item.getPrecaution();
    }
}
