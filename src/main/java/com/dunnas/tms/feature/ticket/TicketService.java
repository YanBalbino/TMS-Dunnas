package com.dunnas.tms.feature.ticket;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.ticket.dto.TicketDto;
import com.dunnas.tms.feature.ticket.dto.TicketRequestDto;
import com.dunnas.tms.feature.ticketStatus.TicketStatus;
import com.dunnas.tms.feature.ticketStatus.TicketStatusRepository;
import com.dunnas.tms.feature.ticketType.TicketType;
import com.dunnas.tms.feature.ticketType.TicketTypeRepository;
import com.dunnas.tms.feature.unit.Unit;
import com.dunnas.tms.feature.unit.UnitRepository;
import com.dunnas.tms.feature.user.UserAccount;
import com.dunnas.tms.feature.user.UserAccountRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketStatusRepository ticketStatusRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UnitRepository unitRepository;
    private final UserAccountRepository userAccountRepository;

    public TicketService(
            TicketRepository ticketRepository,
            TicketStatusRepository ticketStatusRepository,
            TicketTypeRepository ticketTypeRepository,
            UnitRepository unitRepository,
            UserAccountRepository userAccountRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketStatusRepository = ticketStatusRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.unitRepository = unitRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAll() {
        List<TicketDto> allTickets = new ArrayList<>();
        List<Ticket> ticketsFromDatabase = ticketRepository.findAll();

        for (Ticket ticket : ticketsFromDatabase) {
            allTickets.add(TicketDto.fromEntity(ticket));
        }

        return allTickets;
    }

    @Transactional(readOnly = true)
    public TicketDto findById(Long id) {
        return TicketDto.fromEntity(getEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByUnitId(Long unitId) {
        ensureUnitExists(unitId);

        List<TicketDto> ticketsByUnit = new ArrayList<>();
        List<Ticket> ticketsByUnitFromDatabase = ticketRepository.findAllByUnitIdOrderByCreatedAtDesc(unitId);

        for (Ticket ticket : ticketsByUnitFromDatabase) {
            ticketsByUnit.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByUnit;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByUnitNumber(Integer unitNumber) {
        List<TicketDto> ticketsByUnitNumber = new ArrayList<>();
        List<Ticket> ticketsByUnitNumberFromDatabase = ticketRepository.findAllByUnitNumberOrderByCreatedAtDesc(unitNumber);

        for (Ticket ticket : ticketsByUnitNumberFromDatabase) {
            ticketsByUnitNumber.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByUnitNumber;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByAuthorId(Long authorId) {
        ensureUserExists(authorId);

        List<TicketDto> ticketsByAuthor = new ArrayList<>();
        List<Ticket> ticketsByAuthorFromDatabase = ticketRepository.findAllByAuthorIdOrderByCreatedAtDesc(authorId);

        for (Ticket ticket : ticketsByAuthorFromDatabase) {
            ticketsByAuthor.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByAuthor;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByAuthorName(String authorName) {
        List<TicketDto> ticketsByAuthorName = new ArrayList<>();
        List<Ticket> ticketsByAuthorNameFromDatabase = ticketRepository
                .findAllByAuthorNameContainingIgnoreCaseOrderByCreatedAtDesc(authorName);

        for (Ticket ticket : ticketsByAuthorNameFromDatabase) {
            ticketsByAuthorName.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByAuthorName;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByStatusId(Long statusId) {
        ensureStatusExists(statusId);
        List<TicketDto> ticketsByStatus = new ArrayList<>();
        List<Ticket> ticketsByStatusFromDatabase = ticketRepository.findAllByStatusIdOrderByCreatedAtDesc(statusId);

        for (Ticket ticket : ticketsByStatusFromDatabase) {
            ticketsByStatus.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByStatus;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByStatusName(String statusName) {
        List<TicketDto> ticketsByStatusName = new ArrayList<>();
        List<Ticket> ticketsByStatusNameFromDatabase = ticketRepository
                .findAllByStatusNameContainingIgnoreCaseOrderByCreatedAtDesc(statusName);

        for (Ticket ticket : ticketsByStatusNameFromDatabase) {
            ticketsByStatusName.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByStatusName;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByTicketTypeId(Long ticketTypeId) {
        ensureTicketTypeExists(ticketTypeId);
        List<TicketDto> ticketsByTicketType = new ArrayList<>();
        List<Ticket> ticketsByTicketTypeFromDatabase = ticketRepository.findAllByTicketTypeIdOrderByCreatedAtDesc(ticketTypeId);

        for (Ticket ticket : ticketsByTicketTypeFromDatabase) {
            ticketsByTicketType.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByTicketType;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllByTicketTypeTitle(String ticketTypeTitle) {
        List<TicketDto> ticketsByTicketTypeTitle = new ArrayList<>();
        List<Ticket> ticketsByTicketTypeTitleFromDatabase = ticketRepository
                .findAllByTicketTypeTitleContainingIgnoreCaseOrderByCreatedAtDesc(ticketTypeTitle);

        for (Ticket ticket : ticketsByTicketTypeTitleFromDatabase) {
            ticketsByTicketTypeTitle.add(TicketDto.fromEntity(ticket));
        }

        return ticketsByTicketTypeTitle;
    }

    @Transactional(readOnly = true)
    public List<TicketDto> findAllForCollaborator(Long collaboratorId) {
        UserAccount collaborator = getUserById(collaboratorId);

        Set<Long> ticketTypeIds = new HashSet<>();

        for (TicketType ticketType : collaborator.getCollaboratorTicketTypes()) {
            ticketTypeIds.add(ticketType.getId());
        }

        if (ticketTypeIds.isEmpty()) {
            return List.of();
        }

        List<Long> ticketTypeIdsList = new ArrayList<>(ticketTypeIds);

        List<TicketDto> ticketsForCollaborator = new ArrayList<>();
        List<Ticket> ticketsForCollaboratorFromDatabase = ticketRepository.findAllByTicketTypeIdInOrderByCreatedAtDesc(ticketTypeIdsList);

        for (Ticket ticket : ticketsForCollaboratorFromDatabase) {
            ticketsForCollaborator.add(TicketDto.fromEntity(ticket));
        }

        return ticketsForCollaborator;
    }

    @Transactional
    public TicketDto create(TicketRequestDto request) {
        TicketType ticketType = getTicketTypeById(request.ticketTypeId());
        Unit unit = getUnitById(request.unitId());
        UserAccount author = getUserById(request.authorId());

        validateAuthorUnitScope(author, unit);

        TicketStatus defaultStatus = ticketStatusRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new IllegalStateException("No default ticket status configured"));

        Ticket ticket = TicketMapper.toEntity(request);
        ticket.setStatus(defaultStatus);
        ticket.setAuthor(author);
        ticket.setUnit(unit);
        ticket.setTicketType(ticketType);
        ticket.setDueDate(OffsetDateTime.now().plusDays(ticketType.getDeadlineDays()));
        ticket.setCompletedAt(Boolean.TRUE.equals(defaultStatus.getIsFinalizer()) ? OffsetDateTime.now() : null);

        Ticket saved = ticketRepository.save(ticket);
        return TicketDto.fromEntity(saved);
    }

    @Transactional
    public TicketDto update(Long id, TicketRequestDto request) {
        Ticket existing = getEntityById(id);

        TicketStatus status = getStatusById(request.statusId());
        validateImmutableFields(existing, request);

        TicketMapper.updateEntity(existing, request);
        existing.setStatus(status);
        existing.setCompletedAt(resolveCompletedAt(existing, status));

        Ticket saved = ticketRepository.save(existing);
        return TicketDto.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        Ticket existing = getEntityById(id);
        ticketRepository.delete(existing);
    }

    private Ticket getEntityById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found: " + id));
    }

    private TicketStatus getStatusById(Long id) {
        return ticketStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TicketStatus not found: " + id));
    }

    private TicketType getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TicketType not found: " + id));
    }

    private Unit getUnitById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Unit not found: " + id));
    }

    private UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserAccount not found: " + id));
    }

    private void ensureUnitExists(Long unitId) {
        if (!unitRepository.existsById(unitId)) {
            throw new NoSuchElementException("Unit not found: " + unitId);
        }
    }

    private void ensureUserExists(Long userId) {
        if (!userAccountRepository.existsById(userId)) {
            throw new NoSuchElementException("UserAccount not found: " + userId);
        }
    }

    private void ensureStatusExists(Long statusId) {
        if (!ticketStatusRepository.existsById(statusId)) {
            throw new NoSuchElementException("TicketStatus not found: " + statusId);
        }
    }

    private void ensureTicketTypeExists(Long ticketTypeId) {
        if (!ticketTypeRepository.existsById(ticketTypeId)) {
            throw new NoSuchElementException("TicketType not found: " + ticketTypeId);
        }
    }

    private void validateAuthorUnitScope(UserAccount author, Unit unit) {
        if (isResidentRole(author.getRole()) && !unit.getResidents().contains(author)) {
            throw new IllegalStateException("Resident user can only open tickets for linked units");
        }
    }

    private boolean isResidentRole(String role) {
        if (role == null) {
            return false;
        }

        String normalized = role.trim().toUpperCase(Locale.ROOT);
        return "RESIDENT".equals(normalized) || "ROLE_RESIDENT".equals(normalized);
    }

    private void validateImmutableFields(Ticket existing, TicketRequestDto request) {
        boolean authorChanged = !existing.getAuthor().getId().equals(request.authorId());
        boolean unitChanged = !existing.getUnit().getId().equals(request.unitId());
        boolean ticketTypeChanged = !existing.getTicketType().getId().equals(request.ticketTypeId());

        if (authorChanged || unitChanged || ticketTypeChanged) {
            throw new IllegalStateException("Author, unit and ticket type cannot be changed after ticket creation");
        }
    }

    private OffsetDateTime resolveCompletedAt(Ticket existing, TicketStatus nextStatus) {
        if (Boolean.TRUE.equals(nextStatus.getIsFinalizer())) {
            return existing.getCompletedAt() != null ? existing.getCompletedAt() : OffsetDateTime.now();
        }

        return null;
    }


}
