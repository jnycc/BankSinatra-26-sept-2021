package com.miw.model;

public class Asset {

    public Crypto crypto;
    private double units;
    private double currentValue;
    private double deltaDayValue;
    private double deltaMonthValue;
    private double delta3MonthsValue;
    private double deltaYearValue;
    private double deltaStartValue;

    public Asset(Crypto crypto, double units) {
        this.crypto = crypto;
        this.units = units;
//        this.currentValue = calculateValue();
    }

//    private double calculateValue() {
//        return units * crypto.getCryptoPrice();
//    }


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

    public double getDeltaDayValue() {
        return deltaDayValue;
    }

    public void setDeltaDayValue(double deltaDayValue) {
        this.deltaDayValue = deltaDayValue;
    }
}
