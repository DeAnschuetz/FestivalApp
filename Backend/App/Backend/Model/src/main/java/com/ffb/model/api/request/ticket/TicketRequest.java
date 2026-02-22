package com.ffb.model.api.request.ticket;

import java.util.List;

public record TicketRequest(List<String> loginNrs) {
}
