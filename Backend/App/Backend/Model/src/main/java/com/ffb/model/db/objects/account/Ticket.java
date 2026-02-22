package com.ffb.model.db.objects.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ticket", schema = "ffb")
public class Ticket extends PanacheEntityBase {

    @Id
    @Column(name = "ticket_id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "login_nr", unique = true, nullable = false)
    private String loginNr;

    @JsonIgnore
    @OneToOne(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;

    protected Ticket() {}

    public Ticket(UUID id, String loginNr) {
        this.id = id;
        this.loginNr = loginNr;
    }

    public Ticket(UUID id, String loginNr, Account account) {
        this.id = id;
        this.loginNr = loginNr;
        this.account = account;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID ticketNrId) {
        this.id = ticketNrId;
    }

    public String getLoginNr() {
        return loginNr;
    }

    public void setLoginNr(String ticketNr) {
        this.loginNr = ticketNr;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
