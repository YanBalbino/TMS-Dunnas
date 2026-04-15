package com.dunnas.tms.feature.ticket_status;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {

    Optional<TicketStatus> findByIsDefaultTrue();
}
