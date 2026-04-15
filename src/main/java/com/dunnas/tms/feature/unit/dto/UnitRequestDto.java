package com.dunnas.tms.feature.unit.dto;

import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UnitRequestDto(
        @NotNull(message = "Unit number is required")
        Integer number,

        Integer floorNumber,

        @NotNull(message = "Block id is required")
        Long blockId,

        @Size(max = 200, message = "Resident list can have at most 200 users")
        Set<Long> residentIds
) {
}
