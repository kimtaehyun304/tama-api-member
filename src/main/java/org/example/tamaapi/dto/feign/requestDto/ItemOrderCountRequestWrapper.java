package org.example.tamaapi.dto.feign.requestDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOrderCountRequestWrapper {

    @NotEmpty
    private List<ItemOrderCountRequest> itemOrderCountRequests;

}
