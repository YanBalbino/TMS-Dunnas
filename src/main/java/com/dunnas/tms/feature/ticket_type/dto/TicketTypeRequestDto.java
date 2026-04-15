package com.dunnas.tms.feature.ticket_type.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketTypeRequestDto(
        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must have at most 150 characters")
        String title,

        @NotNull(message = "Deadline days is required")
        @Min(value = 1, message = "Deadline days must be at least 1")
        Integer deadlineDays
) {
}
