package com.dunnas.tms.feature.ticket_status;

import com.dunnas.tms.feature.ticket_status.dto.TicketStatusRequestDto;

public final class TicketStatusMapper {

    private TicketStatusMapper() {
    }

    public static TicketStatus toEntity(TicketStatusRequestDto request) {
        return TicketStatus.builder()
                .name(request.name())
                .hexColor(request.hexColor())
                .isDefault(request.isDefault())
                .isFinalizer(request.isFinalizer())
                .build();
    }

    public static void updateEntity(TicketStatus ticketStatus, TicketStatusRequestDto request) {
        ticketStatus.setName(request.name());
        ticketStatus.setHexColor(request.hexColor());
        ticketStatus.setIsDefault(request.isDefault());
        ticketStatus.setIsFinalizer(request.isFinalizer());
    }
}
