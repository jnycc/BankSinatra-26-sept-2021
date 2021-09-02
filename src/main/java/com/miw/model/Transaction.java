package com.miw.model;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private int transactionId;
    private LocalDateTime transactionDate;
    private Client buyer;
    private Client seller;
    private Crypto crypto;
    private double units;
    private double transactionPrice;
    private double bankCosts;

    // price based on units
    public Transaction(Client buyer, Client seller, Crypto crypto, double units) {
        this.buyer = buyer;
        this.seller = seller;
        this.crypto = crypto;
        this.units = units;
        this.transactionPrice = calculatePrice();
        this.transactionDate = LocalDateTime.now();
    }

    // units based on price
    public Transaction(Client buyer, Client seller, Crypto crypto, double price, double bankCosts) {
        this.buyer = buyer;
        this.seller = seller;
        this.crypto = crypto;
        this.transactionPrice = price;
        this.bankCosts = bankCosts;
        this.units = calculateUnits(price);
        this.transactionDate = LocalDateTime.now();
    }

    private double calculatePrice() {
        return units * crypto.getCryptoPrice();
    }

    private double calculateUnits(double price) {
        //TODO: calculate units based on price and course value of cryptocoin - add bank costs?
        return price / crypto.getCryptoPrice();
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public Client getBuyer() {
        return buyer;
    }

    public Client getSeller() {
        return seller;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public double getUnits() {
        return units;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public double getBankCosts() {
        return bankCosts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
