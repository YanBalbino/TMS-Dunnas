package com.dunnas.tms.feature.unit;

import com.dunnas.tms.feature.unit.dto.UnitRequestDto;

public final class UnitMapper {

    private UnitMapper() {
    }

    public static Unit toEntity(UnitRequestDto request) {
        return Unit.builder()
                .number(request.number())
                .floorNumber(request.floorNumber())
                .build();
    }

    public static void updateEntity(Unit unit, UnitRequestDto request) {
        unit.setNumber(request.number());
        unit.setFloorNumber(request.floorNumber());
    }
}
