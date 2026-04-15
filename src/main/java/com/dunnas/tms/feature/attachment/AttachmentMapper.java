package com.dunnas.tms.feature.attachment;

import com.dunnas.tms.feature.attachment.dto.AttachmentRequestDto;

public final class AttachmentMapper {

    private AttachmentMapper() {
    }

    public static Attachment toEntity(AttachmentRequestDto request) {
        return Attachment.builder()
                .fileName(request.fileName())
                .mimeType(request.mimeType())
                .storagePath(request.storagePath())
                .build();
    }

    public static void updateEntity(Attachment attachment, AttachmentRequestDto request) {
        attachment.setFileName(request.fileName());
        attachment.setMimeType(request.mimeType());
        attachment.setStoragePath(request.storagePath());
    }
}
