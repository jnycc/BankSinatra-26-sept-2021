package com.miw.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Client extends User{

    private String dateOfBirth;
    private int bsn;
    private Address address;
    private Map<Crypto, Double> portfolio;
    private Account account;


    public Client(String email, String password, String dateOfBirth, Address address, int bsn) {
        super(email, password);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.bsn = bsn;
        this.account = new Account();
        this.portfolio = new HashMap<>();       // even besluiten welke soort map we gaan gebruiken...
    }

    public Client(String email, String password){
        super(email, password);
    }

    public Client(String email, String password, String firstName, String prefix, String lastName, String dateOfBirth, int bsn, Address address) {
        super(email, password, firstName, prefix, lastName);
        this.dateOfBirth = dateOfBirth;
        this.bsn = bsn;
        this.address = address;
    }

    public Client(String email, String password, String firstName, String prefix, String lastName, int bsn, Address address) {
        super(email, password, firstName, prefix, lastName);
        this.bsn = bsn;
        this.address = address;
    }

    public Client(String email, String firstName, String prefix, String lastName, Address address) {
        super(email, firstName, prefix, lastName);
        this.address = address;
    }

    public Client(String email, String firstName, String prefix, String lastName) {
        super(email, firstName, prefix, lastName);
    }

    public Client() {
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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

}
