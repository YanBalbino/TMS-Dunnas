package com.dunnas.tms.feature.attachment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttachmentRequestDto(
        @NotBlank(message = "File name is required")
        @Size(max = 255, message = "File name must have at most 255 characters")
        String fileName,

        @Size(max = 50, message = "MIME type must have at most 50 characters")
        String mimeType,

        @NotBlank(message = "Storage path is required")
        @Size(max = 255, message = "Storage path must have at most 255 characters")
        String storagePath,

        @NotNull(message = "Ticket id is required")
        Long ticketId
) {
}
