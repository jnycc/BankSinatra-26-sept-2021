package com.miw.model;

public class Asset {

    public Crypto crypto;
    private double units;
    private double currentValue;
    private double deltaValueDay;
    private double deltaValueMonth;
    private double deltaValue3Months;
    private double deltaValueYear;
    private double deltaValueStart;

    public Asset(Crypto crypto, double units) {
        this.crypto = crypto;
        this.units = units;
        this.currentValue = setCalculatedValue();
    }

    private double setCalculatedValue() {
        return units * crypto.getPrice();
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

    public double getDeltaValueDay() {
        return deltaValueDay;
    }

    public void setDeltaValueDay(double deltaValueDay) {
        this.deltaValueDay = deltaValueDay;
    }
}
