package com.miw.controller;

import com.google.gson.Gson;
import com.miw.model.Crypto;
import com.miw.model.Transaction;
import com.miw.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransactionController {

    private TransactionService transactionService;
    private Gson gson;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(TransactionService transactionService, Gson gson){
        super();
        this.transactionService = transactionService;
        this.gson = gson;
        logger.info("New TransactionController-object created");
    }

    //TODO: deze methode is te lang, dus nog een keer opbreken in kleinere methodes
    @PostMapping("/buy") //TODO: URL aanpassen zeer waarschijnlijk
    public ResponseEntity<?> doTransaction(@RequestBody String transactionAsJson){
        //Zet JSON string om naar Transaction
        Transaction transaction = gson.fromJson(transactionAsJson, Transaction.class);

        //Check of aantal units niet negatief is en set de transactionPrice
        try{
            transaction = transactionService.setTransactionPrice(transaction);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("Buyer may not purchase a negative amount of currency. " +
                    "Transaction cannot be completed", HttpStatus.CONFLICT);
        }

        //Haal actueel bankCosts-percentage uit database
        transaction = transactionService.setBankCosts(transaction);

        //TODO: dit even voor overzichtelijkheid gedaan, maar weghalen waarschijnlijk
        int seller = transaction.getSeller();
        int buyer = transaction.getBuyer();
        Crypto crypto = transaction.getCrypto();
        double units = transaction.getUnits();
        double transactionPrice = transaction.getTransactionPrice();
        double bankCosts = transaction.getBankCosts();

        //Check of seller genoeg Crypto heeft en buyer genoeg geld
        if(!transactionService.checkSufficientCrypto(seller, crypto, units)){
            return new ResponseEntity<>("Seller has insufficient assets. Transaction cannot be completed.",
                    HttpStatus.CONFLICT);
        } else if(!transactionService.checkSufficientBalance(seller, buyer, transactionPrice, bankCosts)){
            return new ResponseEntity<>("Buyer has insufficient funds. Transaction cannot be completed.",
                    HttpStatus.CONFLICT);
        }

        //Maak geld, crypto en banking fee over
        transactionService.transferBalance(seller, buyer, transactionPrice);
        transactionService.transferCrypto(seller, buyer, crypto, units);

        //TODO: dit werkt nog niet omdat Bank nog niet in de DB staat. Uitgecomment om te kunnen testen met Postman
        //transactionService.transferBankCosts(seller, buyer, transactionPrice, bankCosts);

        transactionService.registerTransaction(transaction);
        return new ResponseEntity<>("Joepie de poepie, transactie gedaan", HttpStatus.OK);
    }
}
