package com.miw.model;

public class Account {

    // ATTRIBUTES
    private int accountId;
    private String iban;
    private double balance;


    // CONSTRUCTORS

    public Account() {
        this.accountId = generateAccountId();
        this.iban = generateIban();
        this.balance = 10000;
    }


    public Account(double balance) {
        this.iban = generateIban();
        this.balance = balance;
    }


    // METHODS
    private String generateIban() {
        //TODO: create unique iban number
        return "generateIban";
    }


    private int generateAccountId() {
        //TODO: create unique account ID
        return 0;
    }


    // GETTERS & SETTERS
}
