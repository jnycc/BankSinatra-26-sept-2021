package com.miw.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Client extends User{

    @NotNull
    @Past
    private Date dateOfBirth;

    @Range(min = 10000000, max = 999999999)
    //TODO: 11 proef als check toevoegen
    private int bsn;

    @Valid
    private Address address;
    private Map<Crypto, Double> portfolio;
    private Account account = new Account(); // TODO: waarom is account null als dit in de construct zit?

    public Client(String email, String password, String firstName, String prefix, String lastName, Date dateOfBirth, int bsn, Address address) {
        super(email, password, firstName, prefix, lastName);
        this.dateOfBirth = dateOfBirth;
        this.bsn = bsn;
        this.address = address;
        this.portfolio = new HashMap<>();       // TODO: besluiten wat voor soort Map we gaan gebruiken
    }

    public Client() {
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getBsn() {
        return bsn;
    }

    public void setBsn(int bsn) {
        this.bsn = bsn;
    }

    public Address getAddress() {
        return address;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Client{" +
                "account=" + account +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
