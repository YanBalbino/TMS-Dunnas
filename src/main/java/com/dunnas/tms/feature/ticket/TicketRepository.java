package com.dunnas.tms.feature.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByUnitIdOrderByCreatedAtDesc(Long unitId);

    List<Ticket> findAllByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Ticket> findAllByStatusIdOrderByCreatedAtDesc(Long statusId);

    List<Ticket> findAllByTicketTypeIdOrderByCreatedAtDesc(Long ticketTypeId);

    List<Ticket> findAllByTicketTypeIdInOrderByCreatedAtDesc(List<Long> ticketTypeIds);

    boolean existsByAuthorId(Long authorId);
}
