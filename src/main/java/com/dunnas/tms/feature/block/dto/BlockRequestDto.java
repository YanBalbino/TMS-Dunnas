package com.dunnas.tms.feature.block.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BlockRequestDto(
        @NotBlank(message = "Block name is required")
        @Size(max = 50, message = "Block name must have at most 50 characters")
        String name,

        @Size(max = 100, message = "Street must have at most 100 characters")
        String street,

        @Min(value = 1, message = "Number must be at least 1")
        Integer number,

        @NotNull(message = "Floor count is required")
        @Min(value = 1, message = "Floor count must be at least 1")
        Integer floorCount,

        @NotNull(message = "Units per floor is required")
        @Min(value = 1, message = "Units per floor must be at least 1")
        Integer unitsPerFloor
) {
}
