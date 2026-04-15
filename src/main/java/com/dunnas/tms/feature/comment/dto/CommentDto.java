package com.dunnas.tms.feature.comment.dto;

import java.time.OffsetDateTime;

import com.dunnas.tms.feature.comment.Comment;

import lombok.Builder;

@Builder
public record CommentDto(
        Long id,
        String description,
        Long ticketId,
        Long authorId,
        String authorName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .ticketId(comment.getTicket() != null ? comment.getTicket().getId() : null)
                .authorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null)
                .authorName(comment.getAuthor() != null ? comment.getAuthor().getName() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
