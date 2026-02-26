package com.ffb.app.service.api.api.account;

import java.util.List;

import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.account.Ticket;
import com.ffb.model.exception.ServiceException;

public interface AccountService {

	AccountResponse getAccountByLoginNr(String loginNr) throws ServiceException;

	AccountResponse createAccount(String loginNr, String rawPassword) throws ServiceException;

	AccountType verifyAccount(String loginNr, String rawPassword) throws ServiceException;

	List<AccountResponse> getAllAccounts();

	List<TicketResponse> createTicket(List<String> loginNr) throws ServiceException;

	List<TicketResponse> getAllTickets();
}
