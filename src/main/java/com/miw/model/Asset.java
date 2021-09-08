package com.miw.model;

import java.util.Map;
import java.util.TreeMap;

public class Asset {

    private Account account;
    public Crypto crypto;
    private double units;
    private double currentValue;
    private Map<String, Double> historicalNrOfUnits; //interval, units
    private Map<String, Double> historicalValues; //interval, oldValue
    private Map<String, Double> deltaValues;
//    private double delta1DayValue;
//    private double delta1DayPct;
//    private double delta1MonthValue;
//    private double delta1MonthPct;
//    private double delta3MonthsValue;
//    private double delta3MonthsPct;
//    private double deltaYearValue;
//    private double deltaYearPct;
//    private double deltaStartTotalValue;
//    private double deltaStartTotalPct;
//    private AssetDeltaValues assetDeltaValues;

    public Asset(Crypto crypto, double units) {
        this.crypto = crypto;
        this.units = units;
        this.currentValue = calculateValue();
        this.historicalValues = new TreeMap<>();
        this.historicalNrOfUnits = new TreeMap<>();
        this.deltaValues = new TreeMap<>();
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

//    public double getDelta1DayValue() {
//        return delta1DayValue;
//    }
//
//    public void setDelta1DayValue(double delta1DayValue) {
//        this.delta1DayValue = delta1DayValue;
//    }
//
//    public double getDelta1DayPct() {
//        return delta1DayPct;
//    }
//
//    public void setDelta1DayPct(double delta1DayPct) {
//        this.delta1DayPct = delta1DayPct;
//    }
//
//    public double getDelta1MonthValue() {
//        return delta1MonthValue;
//    }
//
//    public void setDelta1MonthValue(double delta1MonthValue) {
//        this.delta1MonthValue = delta1MonthValue;
//    }
//
//    public double getDelta1MonthPct() {
//        return delta1MonthPct;
//    }
//
//    public void setDelta1MonthPct(double delta1MonthPct) {
//        this.delta1MonthPct = delta1MonthPct;
//    }
//
//    public double getDelta3MonthsValue() {
//        return delta3MonthsValue;
//    }
//
//    public void setDelta3MonthsValue(double delta3MonthsValue) {
//        this.delta3MonthsValue = delta3MonthsValue;
//    }
//
//    public double getDelta3MonthsPct() {
//        return delta3MonthsPct;
//    }
//
//    public void setDelta3MonthsPct(double delta3MonthsPct) {
//        this.delta3MonthsPct = delta3MonthsPct;
//    }
//
//    public double getDeltaYearValue() {
//        return deltaYearValue;
//    }
//
//    public void setDeltaYearValue(double deltaYearValue) {
//        this.deltaYearValue = deltaYearValue;
//    }
//
//    public double getDeltaYearPct() {
//        return deltaYearPct;
//    }
//
//    public void setDeltaYearPct(double deltaYearPct) {
//        this.deltaYearPct = deltaYearPct;
//    }
//
//    public double getDeltaStartTotalValue() {
//        return deltaStartTotalValue;
//    }
//
//    public void setDeltaStartTotalValue(double deltaStartTotalValue) {
//        this.deltaStartTotalValue = deltaStartTotalValue;
//    }
//
//    public double getDeltaStartTotalPct() {
//        return deltaStartTotalPct;
//    }
//
//    public void setDeltaStartTotalPct(double deltaStartTotalPct) {
//        this.deltaStartTotalPct = deltaStartTotalPct;
//    }

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
}
