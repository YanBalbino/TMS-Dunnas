package com.dunnas.tms.feature.attachment.dto;

import java.time.OffsetDateTime;

import com.dunnas.tms.feature.attachment.Attachment;

import lombok.Builder;

@Builder
public record AttachmentDto(
        Long id,
        String fileName,
        String mimeType,
        String storagePath,
        Long ticketId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static AttachmentDto fromEntity(Attachment attachment) {
        return AttachmentDto.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .mimeType(attachment.getMimeType())
                .storagePath(attachment.getStoragePath())
                .ticketId(attachment.getTicket() != null ? attachment.getTicket().getId() : null)
                .createdAt(attachment.getCreatedAt())
                .updatedAt(attachment.getUpdatedAt())
                .build();
    }
}
