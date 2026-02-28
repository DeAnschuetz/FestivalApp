package com.ffb.model.api.request.account;

import java.util.UUID;

public record AccountRequest(UUID id, String loginNr, String password) {
}
