package com.dunnas.tms.feature.block.dto;

import java.time.OffsetDateTime;

import com.dunnas.tms.feature.block.Block;

import lombok.Builder;

@Builder
public record BlockDto(
        Long id,
        String name,
        String street,
        Integer number,
        Integer floorCount,
        Integer unitsPerFloor,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static BlockDto fromEntity(Block block) {
        return BlockDto.builder()
                .id(block.getId())
                .name(block.getName())
                .street(block.getStreet())
                .number(block.getNumber())
                .floorCount(block.getFloorCount())
                .unitsPerFloor(block.getUnitsPerFloor())
                .createdAt(block.getCreatedAt())
                .updatedAt(block.getUpdatedAt())
                .build();
    }
}
