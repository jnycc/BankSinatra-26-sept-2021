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

    @Autowired
    public TransactionService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        logger.info("New TransactionService");
    }

    public void registerTransaction(Transaction transaction){
        rootRepository.saveNewTransaction(transaction);
    }

    public boolean checkSufficientBalance(int accountId, double transactionPrice, double bankCosts){
        return rootRepository.getAccountById(accountId).getBalance() >= transactionPrice + (transactionPrice * bankCosts);
    }

    public boolean checkSufficientCrypto(int seller, Crypto crypto, double units){
        // TODO: tidy up code?
       boolean sufficientCrypto = false;
       List<Asset> assets = rootRepository.getAssets(seller);
       for (int i = 0; i < assets.size(); i++) {
            if (assets.get(i).getCrypto().equals(crypto) && assets.get(i).getUnits() >= units){
                sufficientCrypto = true;
            }
        }
        return sufficientCrypto;
    }

    public void transferBalance(int seller, int buyer, double transactionPrice){
        double newSellerBalance = rootRepository.getAccountById(seller).getBalance() + transactionPrice;
        double newBuyerBalance = rootRepository.getAccountById(buyer).getBalance() - transactionPrice;
        rootRepository.updateBalance(newSellerBalance, seller);
        rootRepository.updateBalance(newBuyerBalance, buyer);
    }

    public void transferCrypto(int seller, int buyer, Crypto crypto, double units){
        //TODO: maken zodra JdbcAssetDao een methode update heeft
    }

    public void transferBankCosts(int seller, int buyer, double transactionPrice, double bankCostsPercentage){
        int bankSinatra = Bank.getBankSinatra().getAccount().getAccountId();
        double bankCosts = bankCostsPercentage * transactionPrice;

        //TODO: tidy up code?
        if (seller == bankSinatra){ //seller is bank, buyer is client
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() + bankCosts, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() - bankCosts, buyer);
        } else if (buyer == bankSinatra){ //seller is client, buyer is bank
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() - bankCosts, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() + bankCosts, buyer);
        } else { //both seller and buyer are clients
            double share = bankCosts / 2.0;
            rootRepository.updateBalance(rootRepository.getAccountById(seller).getBalance() - share, seller);
            rootRepository.updateBalance(rootRepository.getAccountById(buyer).getBalance() - share, buyer);
            rootRepository.updateBalance(rootRepository.getAccountById(bankSinatra).getBalance() + bankCosts, bankSinatra);
        }
    }

}
