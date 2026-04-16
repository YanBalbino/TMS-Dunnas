package com.dunnas.tms.feature.ticketStatus;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {

    Optional<TicketStatus> findByIsDefaultTrue();
}
