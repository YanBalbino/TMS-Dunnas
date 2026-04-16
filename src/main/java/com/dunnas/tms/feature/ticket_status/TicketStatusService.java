package com.dunnas.tms.feature.ticket_status;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.ticket_status.dto.TicketStatusDto;
import com.dunnas.tms.feature.ticket_status.dto.TicketStatusRequestDto;

@Service
public class TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

    public TicketStatusService(TicketStatusRepository ticketStatusRepository) {
        this.ticketStatusRepository = ticketStatusRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketStatusDto> findAll() {
        List<TicketStatusDto> allStatuses = new ArrayList<>();
        List<TicketStatus> statusesFromDatabase = ticketStatusRepository.findAll();

        for (TicketStatus status : statusesFromDatabase) {
            allStatuses.add(TicketStatusDto.fromEntity(status));
        }
        
        return allStatuses;
    }

    @Transactional(readOnly = true)
    public TicketStatusDto findById(Long id) {
        TicketStatus ticketStatus = getEntityById(id);

        return TicketStatusDto.fromEntity(ticketStatus);
    }

    @Transactional
    public TicketStatusDto create(TicketStatusRequestDto request) {
        TicketStatus ticketStatus = TicketStatusMapper.toEntity(request);

        enforceSingleDefault(ticketStatus.getIsDefault(), null);
        TicketStatus saved = ticketStatusRepository.save(ticketStatus);

        return TicketStatusDto.fromEntity(saved);
    }

    @Transactional
    public TicketStatusDto update(Long id, TicketStatusRequestDto request) {
        TicketStatus existing = getEntityById(id);
        TicketStatusMapper.updateEntity(existing, request);

        enforceSingleDefault(existing.getIsDefault(), existing.getId());
        TicketStatus saved = ticketStatusRepository.save(existing);

        return TicketStatusDto.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        TicketStatus existing = getEntityById(id);

        ticketStatusRepository.delete(existing);
    }

    private TicketStatus getEntityById(Long id) {
        return ticketStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TicketStatus not found: " + id));
    }

    private void enforceSingleDefault(Boolean isDefaultCandidate, Long currentStatusId) {
        
        // status analisado não é default
        if (!Boolean.TRUE.equals(isDefaultCandidate)) {
            return;
        }

        // já tem um default no banco?
        Optional<TicketStatus> optionalDefault = ticketStatusRepository.findByIsDefaultTrue();

        if (optionalDefault.isPresent()) {
            TicketStatus oldDefault = optionalDefault.get();

            // o default antigo é diferente do status em edição?
            if (!oldDefault.getId().equals(currentStatusId)) {
                oldDefault.setIsDefault(false);
                ticketStatusRepository.save(oldDefault);
            }
        }
    }
}
