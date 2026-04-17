package com.dunnas.tms.feature.ticket.dto;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.dunnas.tms.feature.ticket.Ticket;

import lombok.Builder;

@Builder
public record TicketDto(
        Long id,
        String title,
        String description,
        Long statusId,
        String statusName,
        Long authorId,
        String authorName,
        Long unitId,
        Integer unitNumber,
        Long ticketTypeId,
        String ticketTypeTitle,
        OffsetDateTime dueDate,
        String dueDateFormatted,
        OffsetDateTime completedAt,
        String completedAtFormatted,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("America/Fortaleza");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss");

    public static TicketDto fromEntity(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .statusId(ticket.getStatus() != null ? ticket.getStatus().getId() : null)
                .statusName(ticket.getStatus() != null ? ticket.getStatus().getName() : null)
                .authorId(ticket.getAuthor() != null ? ticket.getAuthor().getId() : null)
                .authorName(ticket.getAuthor() != null ? ticket.getAuthor().getName() : null)
                .unitId(ticket.getUnit() != null ? ticket.getUnit().getId() : null)
                .unitNumber(ticket.getUnit() != null ? ticket.getUnit().getNumber() : null)
                .ticketTypeId(ticket.getTicketType() != null ? ticket.getTicketType().getId() : null)
                .ticketTypeTitle(ticket.getTicketType() != null ? ticket.getTicketType().getTitle() : null)
                .dueDate(ticket.getDueDate())
                .dueDateFormatted(formatDateTime(ticket.getDueDate()))
                .completedAt(ticket.getCompletedAt())
                .completedAtFormatted(formatDateTime(ticket.getCompletedAt()))
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }

    private static String formatDateTime(OffsetDateTime value) {
        if (value == null) {
            return "";
        }

        return value.atZoneSameInstant(DEFAULT_ZONE_ID).format(DATE_TIME_FORMATTER);
    }
}
