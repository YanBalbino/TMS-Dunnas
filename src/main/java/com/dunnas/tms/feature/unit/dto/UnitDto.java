package com.dunnas.tms.feature.unit.dto;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import com.dunnas.tms.feature.unit.Unit;
import com.dunnas.tms.feature.user.UserAccount;

import lombok.Builder;

@Builder
public record UnitDto(
        Long id,
        Integer number,
        Integer floorNumber,
        Long blockId,
        String blockName,
        Set<Long> residentIds,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static UnitDto fromEntity(Unit unit) {
        Set<Long> residentIds = new HashSet<>();

        for (UserAccount resident : unit.getResidents()) {
        residentIds.add(resident.getId());
        }

        return UnitDto.builder()
                .id(unit.getId())
                .number(unit.getNumber())
                .floorNumber(unit.getFloorNumber())
                .blockId(unit.getBlock() != null ? unit.getBlock().getId() : null)
                .blockName(unit.getBlock() != null ? unit.getBlock().getName() : null)
                .residentIds(residentIds)
                .createdAt(unit.getCreatedAt())
                .updatedAt(unit.getUpdatedAt())
                .build();
    }
}
