package com.dunnas.tms.feature.unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.block.Block;
import com.dunnas.tms.feature.block.BlockRepository;
import com.dunnas.tms.feature.ticket.TicketRepository;
import com.dunnas.tms.feature.unit.dto.UnitDto;
import com.dunnas.tms.feature.unit.dto.UnitRequestDto;
import com.dunnas.tms.feature.user.UserAccount;
import com.dunnas.tms.feature.user.UserAccountRepository;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final BlockRepository blockRepository;
    private final UserAccountRepository userAccountRepository;
    private final TicketRepository ticketRepository;

    public UnitService(UnitRepository unitRepository, BlockRepository blockRepository, 
            UserAccountRepository userAccountRepository,
            TicketRepository ticketRepository
    ) {
        this.unitRepository = unitRepository;
        this.blockRepository = blockRepository;
        this.userAccountRepository = userAccountRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<UnitDto> findAll() {
        List<UnitDto> units = new ArrayList<>();
        List<Unit> unitsFromDatabase = unitRepository.findAll();

        for (Unit unit : unitsFromDatabase) {
            units.add(UnitDto.fromEntity(unit));
        }

        return units;
    }

    @Transactional(readOnly = true)
    public List<UnitDto> findAllByBlockId(Long blockId) {
        ensureBlockExists(blockId);

        List<UnitDto> unitsByBlock = new ArrayList<>();
        List<Unit> unitsByBlockFromDatabase = unitRepository.findAllByBlockIdOrderByFloorNumberAscNumberAsc(blockId);

        for (Unit unit : unitsByBlockFromDatabase) {
            unitsByBlock.add(UnitDto.fromEntity(unit));
        }

        return unitsByBlock;
    }

    @Transactional(readOnly = true)
    public UnitDto findById(Long id) {
        Unit unit = getEntityById(id);
        return UnitDto.fromEntity(unit);
    }

    @Transactional
    public UnitDto create(UnitRequestDto request) {
        Block block = getBlockById(request.blockId());
        Set<UserAccount> residents = resolveResidents(request.residentIds());

        Unit unit = UnitMapper.toEntity(request);
        unit.setBlock(block);
        unit.setResidents(residents);

        Unit saved = unitRepository.save(unit);
        return UnitDto.fromEntity(saved);
    }

    @Transactional
    public UnitDto update(Long id, UnitRequestDto request) {
        Unit existing = getEntityById(id);
        Block block = getBlockById(request.blockId());
        Set<UserAccount> residents = resolveResidents(request.residentIds());

        UnitMapper.updateEntity(existing, request);
        existing.setBlock(block);
        existing.setResidents(residents);

        Unit saved = unitRepository.save(existing);
        return UnitDto.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        Unit existing = getEntityById(id);

        if (!ticketRepository.findAllByUnitIdOrderByCreatedAtDesc(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete unit with associated tickets");
        }

        unitRepository.delete(existing);
    }

    private Unit getEntityById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Unit not found: " + id));
    }

    private Block getBlockById(Long id) {
        return blockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Block not found: " + id));
    }

    private void ensureBlockExists(Long blockId) {
        if (!blockRepository.existsById(blockId)) {
            throw new NoSuchElementException("Block not found: " + blockId);
        }
    }

    private Set<UserAccount> resolveResidents(Set<Long> residentIds) {
        if (residentIds == null || residentIds.isEmpty()) {
            return new HashSet<>();
        }

        List<UserAccount> users = userAccountRepository.findAllById(residentIds);
        if (users.size() != residentIds.size()) {
            throw new NoSuchElementException("One or more resident users were not found");
        }

        for (UserAccount user : users) {
            if (!isResidentRole(user.getRole())) {
                throw new IllegalStateException("User is not resident and cannot be linked to unit: " + user.getId());
            }
        }

        return new HashSet<>(users);
    }

    private boolean isResidentRole(String role) {
        if (role == null) {
            return false;
        }

        String normalized = role.trim().toUpperCase(Locale.ROOT);
        return "RESIDENT".equals(normalized) || "ROLE_RESIDENT".equals(normalized);
    }
}
