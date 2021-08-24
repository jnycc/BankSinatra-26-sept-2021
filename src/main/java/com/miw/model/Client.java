package com.miw.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Client extends User{

    private Date dateOfBirth;
    private int bsn;
    private Address address;
    private Map<Crypto, Double> portfolio;
    private Account account;


    public Client(String username, String password, Date dateOfBirth, Address address, int bsn) {
        super(username, password);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.bsn = bsn;
        this.account = new Account();
        this.portfolio = new HashMap<>();       // even besluiten welke soort map we gaan gebruiken...
    }

    public Client(String email, String password){
        super(email, password);
    }


}
