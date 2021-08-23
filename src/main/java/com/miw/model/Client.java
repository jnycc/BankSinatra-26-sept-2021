package com.miw.model;

import java.util.Date;
import java.util.Map;

public class Client extends User{

    //ATTRIBUTES
    private Date dateOfBirth;
    private int bsn;
    private String city;
    private String street;
    private int houseNumber;
    private String houseNumberAdditive;
    private String zipCode;
    private Map<Cryptocoin, Double> portfolio;
    private Account account;


    // CONSTRUCTORS

    public Client(String username, String password, Date dateOfBirth, int bsn, String city, String street,
                  int houseNumber, String houseNumberAdditive, String zipCode) {
        super(username, password);
        this.dateOfBirth = dateOfBirth;
        this.bsn = bsn;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberAdditive = houseNumberAdditive;
        this.zipCode = zipCode;
        this.account = new Account();
        //this.portfolio = new Map<Cryptocoin, Double>;
    }


    // METHODS

    // GETTERS & SETTERS
}
