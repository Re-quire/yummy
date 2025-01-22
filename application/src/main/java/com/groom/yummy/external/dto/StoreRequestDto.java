package com.groom.yummy.external.dto;

import com.groom.yummy.store.Category;
import lombok.Builder;

@Builder
public record StoreRequestDto(
        String name,
        Category category,
        Long regionId
) {
}
