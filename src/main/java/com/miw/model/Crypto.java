package com.miw.model;

import java.util.Objects;

public class Crypto {

    // ATTRIBUTES
    private String name;
    private String symbol;
    private String description;
    private double exchangeRate;


    // CONSTRUCTORS
    public Crypto(String name, String symbol, String description) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.exchangeRate = retrieveValue();
    }

    // METHODS
    private double retrieveValue() {
        //TODO: get recent value of cryptocoin thru API?
        return 0.0;
    }

    // GETTERS & SETTERS
    public double getValue() {
        return exchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return name.equals(crypto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
