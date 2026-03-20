package com.ffb.model.api.request.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(requiredProperties = {
        "loginNrs"
})
public record TicketRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<String> loginNrs
) {
}
