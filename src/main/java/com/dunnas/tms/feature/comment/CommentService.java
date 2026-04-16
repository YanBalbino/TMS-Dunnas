package com.dunnas.tms.feature.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.comment.dto.CommentDto;
import com.dunnas.tms.feature.comment.dto.CommentRequestDto;
import com.dunnas.tms.feature.ticket.Ticket;
import com.dunnas.tms.feature.ticket.TicketRepository;
import com.dunnas.tms.feature.user.UserAccount;
import com.dunnas.tms.feature.user.UserAccountRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserAccountRepository userAccountRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository,
        UserAccountRepository userAccountRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllByTicketId(Long ticketId) {
        List<Comment> commentsFromDatabase = commentRepository.findAllByTicketIdOrderByCreatedAtAsc(ticketId);

        if (!commentsFromDatabase.isEmpty()) {
            List<CommentDto> comments = new ArrayList<>();

            for (Comment comment : commentsFromDatabase) {
                comments.add(CommentDto.fromEntity(comment));
            }

            return comments; 
        }
        ensureTicketExists(ticketId);

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public CommentDto findById(Long id) {
        Comment comment = getEntityById(id);
        return CommentDto.fromEntity(comment);
    }

    @Transactional
    public CommentDto create(CommentRequestDto request) {
        Ticket ticket = getTicketById(request.ticketId());
        UserAccount author = getUserById(request.authorId());

        Comment comment = CommentMapper.toEntity(request);
        comment.setTicket(ticket);
        comment.setAuthor(author);

        Comment saved = commentRepository.save(comment);
        return CommentDto.fromEntity(saved);
    }

    @Transactional
    public CommentDto update(Long id, CommentRequestDto request) {
        Comment existing = getEntityById(id);
        CommentMapper.updateEntity(existing, request);
        Comment saved = commentRepository.save(existing);
        return CommentDto.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        Comment existing = getEntityById(id);
        commentRepository.delete(existing);
    }

    private Comment getEntityById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found: " + id));
    }

    private Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found: " + id));
    }

    private UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserAccount not found: " + id));
    }

    private void ensureTicketExists(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new NoSuchElementException("Ticket not found: " + ticketId);
        }
    }
}
