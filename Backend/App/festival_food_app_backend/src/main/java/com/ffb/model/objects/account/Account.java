package com.ffb.model.objects.account;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account", schema = "ffb")
public class Account extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
    @Column(name = "login_nr")
	private String loginNr;
    
    @Column(name = "password")
	private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
	private AccountType type;
    
    protected Account() {}

	public Account(UUID id, String loginNr, String password, AccountType type) {
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

	public String getLoginNr() {
		return loginNr;
	}

	public void setLoginNr(String loginNr) {
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
