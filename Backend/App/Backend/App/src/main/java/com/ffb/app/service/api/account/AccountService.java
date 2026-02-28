package com.ffb.app.service.api.account;

import java.util.List;
import java.util.Set;
import com.ffb.model.api.request.account.AccountRequest;
import com.ffb.model.api.request.account.LoginRequest;
import com.ffb.model.api.request.account.RegisterRequest;
import com.ffb.model.api.request.ticket.TicketRequest;
import com.ffb.model.api.response.DatabaseResponse;
import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.exception.ServiceException;
import jakarta.transaction.Transactional;

public interface AccountService {

	String verifyAccount(LoginRequest loginRequest) throws ServiceException;

	List<AccountResponse> getAllAccounts();

	@Transactional
	AccountResponse createAccount(RegisterRequest registerRequest) throws ServiceException;

	@Transactional
	List<AccountResponse> createAccounts(List<AccountRequest> accountRegisterRequests);

	@Transactional
	List<TicketResponse> createTickets(TicketRequest loginNr);

	List<TicketResponse> getAllTickets();

	@Transactional
	String createToken(String loginNr, Set<String> roles);

	DatabaseResponse getDatabaseResponse();
}
