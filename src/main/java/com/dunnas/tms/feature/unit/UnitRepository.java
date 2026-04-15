package com.dunnas.tms.feature.unit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findAllByBlockIdOrderByFloorNumberAscNumberAsc(Long blockId);
}
