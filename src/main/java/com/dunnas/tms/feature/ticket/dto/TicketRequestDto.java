package com.dunnas.tms.feature.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketRequestDto(
        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must have at most 150 characters")
        String title,

        @Size(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
        String description,

        @NotNull(message = "Status id is required")
        Long statusId,

        @NotNull(message = "Author id is required")
        Long authorId,

        @NotNull(message = "Unit id is required")
        Long unitId,

        @NotNull(message = "Ticket type id is required")
        Long ticketTypeId
) {
}
