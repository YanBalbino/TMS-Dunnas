package com.dunnas.tms.feature.block;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.block.dto.BlockDto;
import com.dunnas.tms.feature.block.dto.BlockRequestDto;
import com.dunnas.tms.feature.unit.Unit;
import com.dunnas.tms.feature.unit.UnitRepository;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final UnitRepository unitRepository;

    public BlockService(BlockRepository blockRepository, UnitRepository unitRepository) {
        this.blockRepository = blockRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional(readOnly = true)
    public List<BlockDto> findAll() {
        List <BlockDto> blocks = new ArrayList<>();
        List<Block> blocksFromDatabase = blockRepository.findAll();

        for (Block block : blocksFromDatabase) {
            blocks.add(BlockDto.fromEntity(block));
        }

        return blocks;
    }

    @Transactional(readOnly = true)
    public BlockDto findById(Long id) {
        Block block = getEntityById(id);
        return BlockDto.fromEntity(block);
    }

    @Transactional
    public BlockDto create(BlockRequestDto request) {
        Block block = BlockMapper.toEntity(request);
        Block savedBlock = blockRepository.save(block);

        List<Unit> generatedUnits = generateUnitsForBlock(savedBlock);
        unitRepository.saveAll(generatedUnits);

        return BlockDto.fromEntity(savedBlock);
    }

    @Transactional
    public BlockDto update(Long id, BlockRequestDto request) {
        Block existing = getEntityById(id);

        // protege contra alterações de estrutura após a geração das unidades
        if (!existing.getFloorCount().equals(request.floorCount())
                || !existing.getUnitsPerFloor().equals(request.unitsPerFloor())) {
            throw new IllegalStateException("Cannot change floorCount/unitsPerFloor after block creation");
        }

        BlockMapper.updateEntity(existing, request);
        Block saved = blockRepository.save(existing);
        return BlockDto.fromEntity(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {
        Block existing = getEntityById(id);

        if (!unitRepository.findAllByBlockIdOrderByFloorNumberAscNumberAsc(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete block with generated units");
        }

        blockRepository.delete(existing);
    }

    private Block getEntityById(Long id) {
        return blockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Block not found: " + id));
    }

    private List<Unit> generateUnitsForBlock(Block block) {
        List<Unit> units = new ArrayList<>();

        for (int floor = 1; floor <= block.getFloorCount(); floor++) {
            for (int unitPosition = 1; unitPosition <= block.getUnitsPerFloor(); unitPosition++) {
                // Unidades seguem padrão: 101, 102, 201, 202, etc
                int unitNumber = floor * 100 + unitPosition;

                Unit unit = Unit.builder()
                        .number(unitNumber)
                        .floorNumber(floor)
                        .block(block)
                        .build();
                units.add(unit);
            }
        }

        return units;
    }
}
