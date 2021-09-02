package com.miw.service;

import com.miw.database.RootRepository;
import com.miw.model.Client;
import com.miw.model.Crypto;
import com.miw.model.Transaction;
import com.miw.service.authentication.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private RootRepository rootRepository;

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    public TransactionService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        logger.info("New TransactionService");
    }

    public void registerTransaction(Transaction transaction){
        rootRepository.saveNewTransaction(transaction);
    }

    //TODO: uitbreiden met bankkosten erbij (wss buyer en seller meegeven - iets met instanceOf Bank oid)
    public boolean checkSufficientBalance(String email, double transactionPrice){
        return rootRepository.getAccountByEmail(email).getBalance() >= transactionPrice;
    }

    public boolean checkSufficientCrypto(Client seller, Crypto crypto, double units){
        // TODO: aanvullen als we PortfolioDao oid hebben
        //maar ook hier: aantal crypto in portfolio seller moet >= aantal crypto dat de buyer wil kopen
        return false;
    }

    public void transferBalance(Client seller, Client buyer, double price, double bankCosts){
        //TODO: bankkosten nog verrekenen :) Dus ook een getAccountBank-methode in Dao?

        double newSellerBalance = rootRepository.getAccountByEmail(seller.getEmail()).getBalance() + price;
        double newBuyerBalance = rootRepository.getAccountByEmail(buyer.getEmail()).getBalance() - price;

        rootRepository.updateBalance(newSellerBalance, seller.getAccount().getAccountId());
        rootRepository.updateBalance(newBuyerBalance, buyer.getAccount().getAccountId());
    }

    public void transferCrypto(Client seller, Client buyer, Crypto crypto, double units){
        //TODO: zie hierboven maar dan met Crypto. Portfolio-funcionaliteit voor nodig
    }

}
