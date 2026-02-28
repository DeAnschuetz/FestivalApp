package com.ffb.model.api.response.ticket;

import java.util.UUID;

public record TicketResponse(UUID id, String loginNr) {
}
