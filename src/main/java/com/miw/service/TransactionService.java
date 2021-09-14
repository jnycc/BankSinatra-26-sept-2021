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
    private final double HALF = 0.5;

    @Autowired
    public TransactionService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        accountBank = Bank.BANK_ID;
        logger.info("New TransactionService");
    }

    public void registerTransaction(Transaction transaction){
        rootRepository.saveNewTransaction(transaction);
    }

    public Transaction setTransactionPrice(Transaction transaction){
        transaction.getCrypto().setCryptoPrice(rootRepository.getLatestPriceBySymbol(transaction.getCrypto().getSymbol()));
        transaction.setTransactionPrice(transaction.getCrypto().getCryptoPrice() * transaction.getUnits());
        return transaction;
    }

    public Transaction setBankCosts(Transaction transaction){
        transaction.setBankCosts(rootRepository.getBankCosts());
        return transaction;
    }

    public boolean checkSufficientBalance(int seller, int buyer, double transactionPrice, double bankCosts){
        double buyerBalance = rootRepository.getAccountById(buyer).getBalance();
        if (seller == accountBank){
            return buyerBalance >= transactionPrice + (transactionPrice * bankCosts);
        } else if (buyer == accountBank){
            return buyerBalance >= transactionPrice;
        } else {
            return buyerBalance >= transactionPrice + (HALF * (transactionPrice * bankCosts));
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
        double newSellerAsset = rootRepository.getAssetBySymbol(seller, crypto.getSymbol()).getUnits() - units;

        if (!(newSellerAsset == 0)) { // seller still has units of this crypto
            rootRepository.updateAsset(newSellerAsset, crypto.getSymbol(), seller);
        } else { // seller has no units of this crypto anymore
            rootRepository.deleteAsset(crypto.getSymbol(), seller);
        }

        if (rootRepository.getAssetBySymbol(buyer, crypto.getSymbol()) != null) { // buyer already has this crypto
            double newBuyerAsset = rootRepository.getAssetBySymbol(buyer, crypto.getSymbol()).getUnits() + units;
            rootRepository.updateAsset(newBuyerAsset, crypto.getSymbol(), buyer);
        } else { // buyer does not have this crypto yet
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
            double share = bankCosts * HALF;
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() - share, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() - share, buyer);
            rootRepository.updateBalance(rootRepository.getAccountById(accountBank).getBalance() + bankCosts, accountBank);
        }
    }

    public List<Crypto> getCryptoOverview() {
        //Get list of all cryptos
        //Obtain prices minus 1day/1month/3months/year/start
        //Calculate delta values. Hiervoor is een map in Crypto nodig.
        List<Crypto> cryptoOverview = rootRepository.getCryptoOverview();

        return cryptoOverview;
    }
}