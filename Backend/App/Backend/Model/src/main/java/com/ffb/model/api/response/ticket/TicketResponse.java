package com.ffb.model.api.response.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "loginNr"
})
public record TicketResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr
) {
}
