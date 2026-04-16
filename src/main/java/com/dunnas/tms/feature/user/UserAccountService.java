package com.dunnas.tms.feature.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.ticket.TicketRepository;
import com.dunnas.tms.feature.ticket_type.TicketType;
import com.dunnas.tms.feature.ticket_type.TicketTypeRepository;
import com.dunnas.tms.feature.unit.Unit;
import com.dunnas.tms.feature.unit.UnitRepository;
import com.dunnas.tms.feature.user.dto.UserAccountDto;
import com.dunnas.tms.feature.user.dto.UserAccountRequestDto;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UnitRepository unitRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;

    public UserAccountService(
            UserAccountRepository userAccountRepository,
            UnitRepository unitRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketRepository ticketRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.unitRepository = unitRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<UserAccountDto> findAll() {
        List<UserAccountDto> users = new ArrayList<>();
        List<UserAccount> usersFromDatabase = userAccountRepository.findAll();

        for (UserAccount user : usersFromDatabase) {
            users.add(UserAccountDto.fromEntity(user));
        }
        return users;
    }

    @Transactional(readOnly = true)
    public UserAccountDto findById(Long id) {
        return UserAccountDto.fromEntity(getEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<UserAccountDto> findAllByRole(String role) {
        String normalizedRole = normalizeRole(role);

        List<UserAccountDto> usersByRole = new ArrayList<>();

        for (UserAccount user : userAccountRepository.findAllByRoleIgnoreCaseOrderByNameAsc(normalizedRole)) {
            usersByRole.add(UserAccountDto.fromEntity(user));
        }
        return usersByRole;
    }

    @Transactional(readOnly = true)
    public UserAccountDto findByUsername(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("UserAccount not found for username: " + username));
        return UserAccountDto.fromEntity(user);
    }

    @Transactional
    public UserAccountDto create(UserAccountRequestDto request) {
        if (userAccountRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("Username already exists: " + request.username());
        }

        UserAccount user = UserAccountMapper.toEntity(request);
        applyRoleBasedLinks(user, request.unitIds(), request.collaboratorTicketTypeIds());

        UserAccount saved = userAccountRepository.save(user);
        return UserAccountDto.fromEntity(saved);
    }

    @Transactional
    public UserAccountDto update(Long id, UserAccountRequestDto request) {
        UserAccount existing = getEntityById(id);

        if (!existing.getUsername().equals(request.username())
                && userAccountRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("Username already exists: " + request.username());
        }

        UserAccountMapper.updateEntity(existing, request);
        applyRoleBasedLinks(existing, request.unitIds(), request.collaboratorTicketTypeIds());

        UserAccount saved = userAccountRepository.save(existing);
        return UserAccountDto.fromEntity(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {
        UserAccount existing = getEntityById(id);

        if (ticketRepository.existsByAuthorId(id)) {
            throw new IllegalStateException("Cannot delete user with authored tickets");
        }

        userAccountRepository.delete(existing);
    }

    private UserAccount getEntityById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserAccount not found: " + id));
    }

    private void applyRoleBasedLinks(UserAccount user, Set<Long> unitIds, Set<Long> collaboratorTicketTypeIds) {
        Set<Unit> units = resolveUnits(unitIds);
        Set<TicketType> ticketTypes = resolveTicketTypes(collaboratorTicketTypeIds);

        String role = normalizeRole(user.getRole());
        user.setRole(role);

        if (isAdminRole(role)) {
            user.setUnits(new HashSet<>());
            user.setCollaboratorTicketTypes(new HashSet<>());
            return;
        }

        if (isResidentRole(role)) {
            user.setUnits(units);
            user.setCollaboratorTicketTypes(new HashSet<>());
            return;
        }

        if (isCollaboratorRole(role)) {
            user.setUnits(new HashSet<>());
            user.setCollaboratorTicketTypes(ticketTypes);
            return;
        }

        throw new IllegalStateException("Unsupported user role: " + role);
    }

    private Set<Unit> resolveUnits(Set<Long> unitIds) {
        if (unitIds == null || unitIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Unit> units = unitRepository.findAllById(unitIds);
        if (units.size() != unitIds.size()) {
            throw new NoSuchElementException("One or more units were not found");
        }

        return new HashSet<>(units);
    }

    private Set<TicketType> resolveTicketTypes(Set<Long> ticketTypeIds) {
        if (ticketTypeIds == null || ticketTypeIds.isEmpty()) {
            return new HashSet<>();
        }

        List<TicketType> ticketTypes = ticketTypeRepository.findAllById(ticketTypeIds);
        if (ticketTypes.size() != ticketTypeIds.size()) {
            throw new NoSuchElementException("One or more ticket types were not found");
        }

        return new HashSet<>(ticketTypes);
    }

    private String normalizeRole(String role) {
        String normalized = role.trim().toUpperCase(Locale.ROOT);
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }

    private boolean isAdminRole(String role) {
        return "ROLE_ADMIN".equals(role);
    }

    private boolean isResidentRole(String role) {
        return "ROLE_RESIDENT".equals(role);
    }

    private boolean isCollaboratorRole(String role) {
        return "ROLE_COLLABORATOR".equals(role);
    }
}
