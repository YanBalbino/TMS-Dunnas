package com.dunnas.tms.feature.attachment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findAllByTicketId(Long ticketId);
}
