package com.miw.model;

import java.util.Objects;

public class Crypto {

    private int cryptoId;
    private String name;
    private String symbol;
    private String description;
    private double price;

    public Crypto(String name, String symbol, String description) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.price = retrieveValue();
    }

    private double retrieveValue() {
        //TODO: get recent value of cryptocoin thru API?
        return 0.0;
    }

    public int getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(int cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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
