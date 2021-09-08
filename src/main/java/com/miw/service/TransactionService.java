package com.miw.service;

import com.miw.database.RootRepository;
import com.miw.model.*;
import com.miw.service.authentication.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private RootRepository rootRepository;
    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private int accountBank;

    @Autowired
    public TransactionService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        accountBank = Bank.getBankSinatra().getAccount().getAccountId();
        logger.info("New TransactionService");
    }

    public void registerTransaction(Transaction transaction){
        rootRepository.saveNewTransaction(transaction);
    }

    public Transaction setTransactionPrice(Transaction transaction){
        if(transaction.getUnits() < 0){
            throw new IllegalArgumentException("Units may not be negative");
        } else {
            transaction.setTransactionPrice(transaction.getCrypto().getCryptoPrice() * transaction.getUnits());
            return transaction;
        }
    }

    public boolean checkSufficientBalance(int seller, int buyer, double transactionPrice, double bankCosts){
        double buyerBalance = rootRepository.getAccountById(buyer).getBalance();
        if (seller == accountBank){
            return buyerBalance >= transactionPrice + (transactionPrice * bankCosts);
        } else if (buyer == accountBank){
            return buyerBalance >= transactionPrice;
        } else {
            return buyerBalance >= transactionPrice + (0.5 * (transactionPrice * bankCosts));
        }
    }

    public boolean checkSufficientCrypto(int seller, Crypto crypto, double units){
        return rootRepository.getAssetBySymbol(seller, crypto.getSymbol()).getUnits() >= units;
    }

    public void transferBalance(int seller, int buyer, double transactionPrice){
        rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() + transactionPrice, seller);
        rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() - transactionPrice, buyer);
    }

    public void transferCrypto(int seller, int buyer, Crypto crypto, double units){
        //TODO: maken zodra JdbcAssetDao een methode update heeft
        double newSellerAsset = rootRepository.getAssetBySymbol(seller, crypto.getSymbol()).getUnits() - units;
        rootRepository.updateAsset(newSellerAsset, crypto.getSymbol(), seller);
        if (rootRepository.getAssetBySymbol(buyer, crypto.getSymbol()) != null) { // buyer already has crypto
            double newBuyerAsset = rootRepository.getAssetBySymbol(buyer, crypto.getSymbol()).getUnits() + units;
            rootRepository.updateAsset(newBuyerAsset, crypto.getSymbol(), buyer);
        } else { // buyer does not have crypto yet
            rootRepository.saveAsset(buyer, crypto.getSymbol(), units);
        }
    }

    public void transferBankCosts(int seller, int buyer, double transactionPrice, double bankCostsPercentage){
        double bankCosts = bankCostsPercentage * transactionPrice;

        //TODO: tidy up code?
        if (seller == accountBank){ //seller is bank, buyer is client
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() + bankCosts, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() - bankCosts, buyer);
        } else if (buyer == accountBank){ //seller is client, buyer is bank
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() - bankCosts, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() + bankCosts, buyer);
        } else { //both seller and buyer are clients
            double share = bankCosts * 0.5;
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() - share, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() - share, buyer);
            rootRepository.updateBalance(rootRepository.getAccountById(accountBank).getBalance() + bankCosts, accountBank);
        }
    }
}