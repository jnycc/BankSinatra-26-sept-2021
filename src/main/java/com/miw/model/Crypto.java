package com.miw.model;

import java.util.Objects;

public class Crypto implements Comparable<Crypto>{

    private int cryptoId;
    private String name;
    private String symbol;
    private String description;
    private double cryptoPrice;

    public Crypto(String name, String symbol, String description, Double cryptoPrice) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.cryptoPrice = cryptoPrice;
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

    public double getCryptoPrice() {
        return cryptoPrice;
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

    @Override
    public String toString() {
        return "Crypto{" +
                "cryptoId=" + cryptoId +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                ", cryptoPrice=" + cryptoPrice +
                '}';
    }

    @Override
    public int compareTo(Crypto o) {
        return this.cryptoId - o.getCryptoId();
    }
}
