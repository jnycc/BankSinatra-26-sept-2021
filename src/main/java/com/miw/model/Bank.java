package com.miw.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

@Component
public class Bank {
    private Random random;
    private Account account;
    private Map<Crypto, Double> portfolio;
    private final double START_CAPITAL = 5000000;
    private final int NR_ASSETS_MIN = 1000;
    private final int NR_ASSETS_MAX = 5000;
    private DecimalFormat df = new DecimalFormat("#.##");

    private Logger logger = LoggerFactory.getLogger(Bank.class);

    public Bank(Set<Crypto> cryptos) {
        this.account = new Account(START_CAPITAL);
        this.random = new Random();
        setUpPortfolio(cryptos);
        logger.info("Bank Sinatra is founded");
    }

    public Bank() {
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

    public double getSTART_CAPITAL() {
        return START_CAPITAL;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "account=" + account +
                ", portfolio=" + portfolio +
                ", START_CAPITAL=" + START_CAPITAL +
                '}';
    }

    // TODO Map type naar TreeMap omzetten zodra daadwerkelijke Cryptos zijn gemaakt
    public void setUpPortfolio(Set<Crypto> cryptos) {
        this.portfolio = new HashMap<>();
        cryptos.forEach(c -> this.portfolio.put(c, Double.parseDouble(df.format(NR_ASSETS_MIN + (NR_ASSETS_MAX - NR_ASSETS_MIN) * random.nextDouble()))));
    }
}
