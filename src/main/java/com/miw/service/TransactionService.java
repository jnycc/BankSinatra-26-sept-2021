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
        return rootRepository.getAccountById(accountId).getBalance() >= transactionPrice * bankCosts;
    }

    public boolean checkSufficientCrypto(int seller, Crypto crypto, double units){
        // TODO: tidy up code?
       boolean sufficientCrypto = false;
       List<Asset> assets = rootRepository.getAssets(seller);
       for (int i = 0; i < assets.size(); i++) {
            if (assets.get(i).getCrypto() == crypto){
                if (assets.get(i).getUnits() >= units){
                    sufficientCrypto = true;
                }
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

    //TODO: A) Is Bank accountId 1? B) Op een andere manier Bank account ophalen?
    public void transferBankCosts(int seller, int buyer, double transactionPrice, double bankCostsPercentage){
        double bankCosts = bankCostsPercentage * transactionPrice;

        //TODO: tidy up code
        if (seller == 1){ //seller is bank, buyer is client
            double newSellerBalance = rootRepository.getAccountById(1).getBalance() + bankCosts;
            double newBuyerBalance = rootRepository.getAccountById(buyer).getBalance() - bankCosts;
            rootRepository.updateBalance(newSellerBalance, 1);
            rootRepository.updateBalance(newBuyerBalance, buyer);
        } else if (buyer == 1){ //seller is client, buyer is bank
            double newSellerBalance = rootRepository.getAccountById(seller).getBalance() - bankCosts;
            double newBuyerBalance = rootRepository.getAccountById(1).getBalance() + bankCosts;
            rootRepository.updateBalance(newSellerBalance, seller);
            rootRepository.updateBalance(newBuyerBalance, 1);
        } else { //both seller and buyer are clients
            double share = bankCosts / 2.0;
            double newSellerBalance = rootRepository.getAccountById(seller).getBalance() - share;
            double newBuyerBalance = rootRepository.getAccountById(buyer).getBalance() - share;
            double newBankBalance = rootRepository.getAccountById(1).getBalance() + bankCosts;
            rootRepository.updateBalance(newSellerBalance, seller);
            rootRepository.updateBalance(newBuyerBalance, buyer);
            rootRepository.updateBalance(newBankBalance, 1);
        }
    }

}
