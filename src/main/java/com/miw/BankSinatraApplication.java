package com.miw;

import com.miw.database.RootRepository;
import com.miw.model.Account;
import com.miw.model.Asset;
import com.miw.model.Bank;
import com.miw.model.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;

@EnableScheduling
@SpringBootApplication
public class BankSinatraApplication {

    private Logger logger = LoggerFactory.getLogger(BankSinatraApplication.class);
    public static final int BANK_ID = 1;
    private RootRepository repository;

    @Autowired
    public BankSinatraApplication(RootRepository repository) {
        this.repository = repository;
        Bank.getBankSinatra();
        Map<Crypto, Double> portfolio = new TreeMap<>();
        Account bankSinatra = repository.getAccountById(BANK_ID);
        List<Asset> assetList = repository.getAssets(BANK_ID);

        for (Asset asset : assetList) {
            portfolio.put(asset.getCrypto(), asset.getUnits());
        }

        Bank.getBankSinatra().setPortfolio(portfolio);
        Bank.getBankSinatra().setAccount(bankSinatra);

        logger.info("Bank Sinatra is founded");
    }

    public static void main(String[] args) {
        SpringApplication.run(BankSinatraApplication.class, args);
    }
}
