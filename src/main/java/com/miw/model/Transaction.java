package com.miw.model;


import java.time.LocalDateTime;

public class Transaction {

    // ATTRIBUTES
    private int transactionNumber;
    private LocalDateTime transactionDate;
    private Client buyer;
    private Client seller;
    private Cryptocoin cryptocoin;
    private double units;
    private double price;
    private double bankCosts;


    // CONSTRUCTORS

    // price based on units
    public Transaction(Client buyer, Client seller, Cryptocoin cryptocoin, double units) {
        this.buyer = buyer;
        this.seller = seller;
        this.cryptocoin = cryptocoin;
        this.units = units;
        this.price = calculatePrice(units);
        this.transactionDate = LocalDateTime.now();
        this.transactionNumber = generateTransactionNumber();
    }


    // units based on price
    public Transaction(Client buyer, Client seller, Cryptocoin cryptocoin, double price, double bankCosts) {
        this.buyer = buyer;
        this.seller = seller;
        this.cryptocoin = cryptocoin;
        this.price = price;
        this.bankCosts = bankCosts;
        this.units = calculateUnits(price);
        this.transactionDate = LocalDateTime.now();
    }



    // METHODS
    private double calculatePrice(double units) {
        //TODO: calculate price based on unit and course value of cryptocoin
        return 0.0;
    }

    private double calculateUnits(double price) {
        //TODO: calculate price based on unit and course value of cryptocoin
        return 0.0;
    }

    private int generateTransactionNumber() {
        //TODO: generate unique transactionNumber
        return 0;
    }

    // GETTERS & SETTERS
}
