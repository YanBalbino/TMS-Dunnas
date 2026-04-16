package com.dunnas.tms.feature.ticketType.dto;

import java.time.OffsetDateTime;

import com.dunnas.tms.feature.ticketType.TicketType;

import lombok.Builder;

@Builder
public record TicketTypeDto(
        Long id,
        String title,
        Integer deadlineDays,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static TicketTypeDto fromEntity(TicketType ticketType) {
        return TicketTypeDto.builder()
                .id(ticketType.getId())
                .title(ticketType.getTitle())
                .deadlineDays(ticketType.getDeadlineDays())
                .createdAt(ticketType.getCreatedAt())
                .updatedAt(ticketType.getUpdatedAt())
                .build();
    }
}
