package com.dunnas.tms.feature.ticket_status.dto;

import java.time.OffsetDateTime;

import com.dunnas.tms.feature.ticket_status.TicketStatus;

import lombok.Builder;

@Builder
public record TicketStatusDto(
        Long id,
        String name,
        String hexColor,
        Boolean isDefault,
        Boolean isFinalizer,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static TicketStatusDto fromEntity(TicketStatus ticketStatus) {
        return TicketStatusDto.builder()
                .id(ticketStatus.getId())
                .name(ticketStatus.getName())
                .hexColor(ticketStatus.getHexColor())
                .isDefault(ticketStatus.getIsDefault())
                .isFinalizer(ticketStatus.getIsFinalizer())
                .createdAt(ticketStatus.getCreatedAt())
                .updatedAt(ticketStatus.getUpdatedAt())
                .build();
    }
}
