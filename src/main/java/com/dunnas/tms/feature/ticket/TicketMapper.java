package com.dunnas.tms.feature.ticket;

import com.dunnas.tms.feature.ticket.dto.TicketRequestDto;

public final class TicketMapper {

    private TicketMapper() {
    }

    public static Ticket toEntity(TicketRequestDto request) {
        return Ticket.builder()
                .title(request.title())
                .description(request.description())
                .build();
    }

    public static void updateEntity(Ticket ticket, TicketRequestDto request) {
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
    }
}
