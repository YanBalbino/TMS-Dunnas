package com.dunnas.tms.feature.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @NotBlank(message = "Description is required")
        @Size(max = 2000, message = "Description must have at most 2000 characters")
        String description,

        @NotNull(message = "Ticket id is required")
        Long ticketId
) {
}
