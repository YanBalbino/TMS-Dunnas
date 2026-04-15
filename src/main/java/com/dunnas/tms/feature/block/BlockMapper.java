package com.dunnas.tms.feature.block;

import com.dunnas.tms.feature.block.dto.BlockRequestDto;

public final class BlockMapper {

    private BlockMapper() {
    }

    public static Block toEntity(BlockRequestDto request) {
        return Block.builder()
                .name(request.name())
                .street(request.street())
                .number(request.number())
                .floorCount(request.floorCount())
                .unitsPerFloor(request.unitsPerFloor())
                .build();
    }

    public static void updateEntity(Block block, BlockRequestDto request) {
        block.setName(request.name());
        block.setStreet(request.street());
        block.setNumber(request.number());
        block.setFloorCount(request.floorCount());
        block.setUnitsPerFloor(request.unitsPerFloor());
    }
}
