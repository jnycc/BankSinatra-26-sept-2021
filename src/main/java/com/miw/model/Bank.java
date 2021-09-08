package com.miw.model;

import java.util.*;

public class Bank {
    private static Bank bankSinatra = null;
    private Random random;
    private Account account;
    private Map<Crypto, Double> portfolio;
    private final double START_CAPITAL = 5000000;
    private final int NR_ASSETS_MIN = 1000;
    private final int NR_ASSETS_MAX = 5000;

    private Bank() {
        this.account = new Account(START_CAPITAL);
        this.random = new Random();
        this.portfolio = new TreeMap<>();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Map<Crypto, Double> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Map<Crypto, Double> portfolio) {
        this.portfolio = portfolio;
    }


    @Override
    public String toString() {
        return "Bank{" +
                "account=" + account +
                ", portfolio=" + portfolio + '}';
    }

    public void setUpInitialPortfolio(Set<Crypto> cryptos) {
        cryptos.forEach(c -> this.portfolio.put(c, (NR_ASSETS_MIN + (NR_ASSETS_MAX - NR_ASSETS_MIN) * random.nextDouble())));
    }

    public static Bank getBankSinatra() {
        if (bankSinatra == null) {
            bankSinatra = new Bank();
        }
        return bankSinatra;
    }
}
