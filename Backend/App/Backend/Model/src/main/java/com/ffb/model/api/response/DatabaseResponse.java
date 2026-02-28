package com.ffb.model.api.response;

import com.ffb.model.api.response.account.AccountResponseFull;
import java.util.List;

public record DatabaseResponse(List<AccountResponseFull> accounts) {
}
