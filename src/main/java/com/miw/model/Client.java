package com.miw.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Client extends User{

    private Date dateOfBirth;
    private int bsn;
    private Addres addres;
    private Map<Cryptocoin, Double> portfolio;
    private Account account;


    public Client(String username, String password, Date dateOfBirth, Addres addres, int bsn) {
        super(username, password);
        this.dateOfBirth = dateOfBirth;
        this.addres = addres;
        this.bsn = bsn;
        this.account = new Account();
        this.portfolio = new HashMap<>();       // even besluiten welke soort map we gaan gebruiken...
    }

}
