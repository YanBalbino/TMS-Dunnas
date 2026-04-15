package com.dunnas.tms.feature.comment;

import com.dunnas.tms.feature.comment.dto.CommentRequestDto;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toEntity(CommentRequestDto request) {
        return Comment.builder()
                .description(request.description())
                .build();
    }

    public static void updateEntity(Comment comment, CommentRequestDto request) {
        comment.setDescription(request.description());
    }
}
