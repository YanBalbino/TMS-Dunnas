package com.dunnas.tms.feature.user.dto;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import com.dunnas.tms.feature.user.UserAccount;
import com.dunnas.tms.feature.ticketType.TicketType;
import com.dunnas.tms.feature.unit.Unit;

import lombok.Builder;

@Builder
public record UserAccountDto(
        Long id,
        String name,
        String username,
        String role,
        Set<Long> unitIds,
        Set<Long> collaboratorTicketTypeIds,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static UserAccountDto fromEntity(UserAccount userAccount) {
        Set<Long> unitIds = new HashSet<>();

        // TODO: verificar se gera problemas com esses get
        for (Unit unit : userAccount.getUnits()) {
            unitIds.add(unit.getId());
        }

        Set<Long> collaboratorTicketTypeIds = new HashSet<>();

        for (TicketType ticketType : userAccount.getCollaboratorTicketTypes()) {
            collaboratorTicketTypeIds.add(ticketType.getId());
        }

        return UserAccountDto.builder()
                .id(userAccount.getId())
                .name(userAccount.getName())
                .username(userAccount.getUsername())
                .role(userAccount.getRole())
                .unitIds(unitIds)
                .collaboratorTicketTypeIds(collaboratorTicketTypeIds)
                .createdAt(userAccount.getCreatedAt())
                .updatedAt(userAccount.getUpdatedAt())
                .build();
    }
}
