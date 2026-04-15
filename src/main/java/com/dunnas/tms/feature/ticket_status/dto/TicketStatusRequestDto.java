package com.dunnas.tms.feature.ticket_status.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TicketStatusRequestDto(
        @NotBlank(message = "Status name is required")
        @Size(max = 50, message = "Status name must have at most 50 characters")
        String name,

        @Pattern(
                regexp = "^#[0-9A-Fa-f]{6}$",
                message = "Hex color must follow #RRGGBB format"
        )
        String hexColor,

        Boolean isDefault,
        Boolean isFinalizer
) {
}
