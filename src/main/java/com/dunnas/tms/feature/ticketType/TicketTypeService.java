package com.dunnas.tms.feature.ticketType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.ticketType.dto.TicketTypeDto;
import com.dunnas.tms.feature.ticketType.dto.TicketTypeRequestDto;

@Service
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketTypeDto> findAll() {
        List<TicketTypeDto> allTypes = new ArrayList<>();
        List<TicketType> ticketTypesFromDatabase = ticketTypeRepository.findAll();

        for (TicketType ticketType : ticketTypesFromDatabase) {
            allTypes.add(TicketTypeDto.fromEntity(ticketType));
        }

        return allTypes;
    }

    @Transactional(readOnly = true)
    public TicketTypeDto findById(Long id) {
        TicketType ticketType = getEntityById(id);

        return TicketTypeDto.fromEntity(ticketType);
    }

    @Transactional
    public TicketTypeDto create(TicketTypeRequestDto request) {
        TicketType ticketType = TicketTypeMapper.toEntity(request);
        TicketType saved = ticketTypeRepository.save(ticketType);

        return TicketTypeDto.fromEntity(saved);
    }

    @Transactional
    public TicketTypeDto update(Long id, TicketTypeRequestDto request) {
        TicketType existing = getEntityById(id);
        TicketTypeMapper.updateEntity(existing, request);
        TicketType saved = ticketTypeRepository.save(existing);

        return TicketTypeDto.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        TicketType existing = getEntityById(id);
        ticketTypeRepository.delete(existing);
    }

    private TicketType getEntityById(Long id) {
        return ticketTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TicketType not found: " + id));
    }
}
