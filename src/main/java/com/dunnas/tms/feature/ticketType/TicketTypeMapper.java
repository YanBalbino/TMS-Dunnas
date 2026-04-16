package com.dunnas.tms.feature.ticketType;

import com.dunnas.tms.feature.ticketType.dto.TicketTypeRequestDto;

public final class TicketTypeMapper {

    private TicketTypeMapper() {
    }

    public static TicketType toEntity(TicketTypeRequestDto request) {
        return TicketType.builder()
                .title(request.title())
                .deadlineDays(request.deadlineDays())
                .build();
    }

    public static void updateEntity(TicketType ticketType, TicketTypeRequestDto request) {
        ticketType.setTitle(request.title());
        ticketType.setDeadlineDays(request.deadlineDays());
    }
}
