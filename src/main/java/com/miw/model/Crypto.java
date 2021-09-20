package com.miw.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Crypto implements Comparable<Crypto>{

    private String name;
    private String symbol;
    private String description;
    private double cryptoPrice;
    private LocalDateTime dateRetrieved;

    public Crypto(String name, String symbol, String description, Double cryptoPrice) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.cryptoPrice = cryptoPrice;
    }

    public Crypto(){}

    private double retrieveValue() {
        //TODO: get recent value of cryptocoin thru API?
        return 0.0;
    }


    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCryptoPrice() {
        return cryptoPrice;
    }

    public void setCryptoPrice(double cryptoPrice) {
        this.cryptoPrice = cryptoPrice;
    }

    public LocalDateTime getDateRetrieved() {
        return dateRetrieved;
    }

    public void setDateRetrieved(LocalDateTime dateRetrieved) {
        this.dateRetrieved = dateRetrieved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return symbol.equals(crypto.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Crypto{" +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", cryptoPrice=" + cryptoPrice +
                '}';
    }

    @Override
    public int compareTo(Crypto o) {
        return this.name.compareTo(o.getName());
    }
}
