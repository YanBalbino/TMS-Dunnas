package com.dunnas.tms.feature.attachment;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dunnas.tms.feature.attachment.dto.AttachmentDto;
import com.dunnas.tms.feature.attachment.dto.AttachmentRequestDto;
import com.dunnas.tms.feature.ticket.Ticket;
import com.dunnas.tms.feature.ticket.TicketRepository;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TicketRepository ticketRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, TicketRepository ticketRepository) {
        this.attachmentRepository = attachmentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<AttachmentDto> findAllByTicketId(Long ticketId) {
        List<Attachment> attachmentsFromDatabase = attachmentRepository.findAllByTicketId(ticketId);

        if (!attachmentsFromDatabase.isEmpty()) {
            List<AttachmentDto> attachments = new ArrayList<>();

            for (Attachment attachment : attachmentsFromDatabase) {
                attachments.add(AttachmentDto.fromEntity(attachment));
            }

            return attachments; 
        }

        ensureTicketExists(ticketId);

        return new ArrayList<>(); 
    }

    @Transactional(readOnly = true)
    public AttachmentDto findById(Long id) {
        Attachment attachment = getEntityById(id);
        return AttachmentDto.fromEntity(attachment);
    }

    @Transactional
    public AttachmentDto create(AttachmentRequestDto request) {
        Ticket ticket = getTicketById(request.ticketId());

        Attachment attachment = AttachmentMapper.toEntity(request);
        attachment.setTicket(ticket);

        Attachment saved = attachmentRepository.save(attachment);
        return AttachmentDto.fromEntity(saved);
    }

    @Transactional
    public AttachmentDto update(Long id, AttachmentRequestDto request) {
        Attachment existing = getEntityById(id);
        AttachmentMapper.updateEntity(existing, request);
        Attachment saved = attachmentRepository.save(existing);
        return AttachmentDto.fromEntity(saved);
    }

    @Transactional
    public void delete(Long id) {
        Attachment existing = getEntityById(id);
        attachmentRepository.delete(existing);
    }

    private Attachment getEntityById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Attachment not found: " + id));
    }

    private Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found: " + id));
    }

    private void ensureTicketExists(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new NoSuchElementException("Ticket not found: " + ticketId);
        }
    }
}
