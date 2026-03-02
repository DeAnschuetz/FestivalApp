package com.ffb.model.api.response.credit;

import java.util.List;
import java.util.UUID;

public record CreditResponseFull(UUID id, double amount, List<CreditHistoryResponse> history) {
}
