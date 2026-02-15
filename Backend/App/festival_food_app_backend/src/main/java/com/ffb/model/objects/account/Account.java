package com.ffb.model.objects.account;

import java.util.UUID;

public class Account {

	private UUID id;
	private UUID loginNr;
	private String password;
	private AccountType type;

	public Account(UUID id, UUID loginNr, String password, AccountType type) {
		super();
		this.id = id;
		this.loginNr = loginNr;
		this.password = password;
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getLoginNr() {
		return loginNr;
	}

	public void setLoginNr(UUID loginNr) {
		this.loginNr = loginNr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "account [id=" + id + ", loginNr=" + loginNr + ", password=" + password + ", type=" + type + "]";
	}

}
