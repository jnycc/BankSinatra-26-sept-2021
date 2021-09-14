package com.miw.model;

import java.util.Map;
import java.util.TreeMap;

public class Asset {

    private Account account;
    public Crypto crypto;
    private double units;
    private double currentValue;
    private double unitsForSale;
    private double salePrice;
    private Map<String, Double> historicalNrOfUnits; //time interval, units
    private Map<String, Double> historicalValues; //time interval, oldValue
    private Map<String, Double> deltaValues;
    private final int DEFAULT_UNITS = 0;


    public Asset(Crypto crypto, double units) {
        this.crypto = crypto;
        this.units = units;
        this.currentValue = calculateValue();
        this.historicalValues = new TreeMap<>();
        this.historicalNrOfUnits = new TreeMap<>();
        this.deltaValues = new TreeMap<>();
        this.unitsForSale = DEFAULT_UNITS;
        this.salePrice = DEFAULT_UNITS;
    }

    private double calculateValue() {
        return units * crypto.getCryptoPrice();
    }


    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public Map<String, Double> getHistoricalValues() {
        return historicalValues;
    }

    public void setHistoricalValues(Map<String, Double> historicalValues) {
        this.historicalValues = historicalValues;
    }

    public Map<String, Double> getHistoricalNrOfUnits() {
        return historicalNrOfUnits;
    }

    public void setHistoricalNrOfUnits(Map<String, Double> historicalNrOfUnits) {
        this.historicalNrOfUnits = historicalNrOfUnits;
    }

    public Map<String, Double> getDeltaValues() {
        return deltaValues;
    }

    public void setDeltaValues(Map<String, Double> deltaValues) {
        this.deltaValues = deltaValues;
    }

    public double getUnitsForSale() {
        return unitsForSale;
    }

    public void setUnitsForSale(double unitsForSale) {
        this.unitsForSale = unitsForSale;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}
