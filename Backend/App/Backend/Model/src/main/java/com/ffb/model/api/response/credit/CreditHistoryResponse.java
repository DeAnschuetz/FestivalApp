package com.ffb.model.api.response.credit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditHistoryResponse(double oldAmount, double newAmount, LocalDateTime changeTime) {
}
