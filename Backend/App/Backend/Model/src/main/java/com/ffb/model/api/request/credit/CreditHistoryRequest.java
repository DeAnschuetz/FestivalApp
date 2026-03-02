package com.ffb.model.api.request.credit;

public record CreditHistoryRequest(String loginNr, int pageIndex, int pageSize) {
}
