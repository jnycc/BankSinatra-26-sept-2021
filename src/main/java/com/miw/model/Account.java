package com.miw.model;

public class Account {

    // ATTRIBUTES
    private String iban;
    private double balance;


    // CONSTRUCTORS

    public Account() {
        this.iban = generateIban();
        this.balance = 1000;

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


    // GETTERS & SETTERS
}
