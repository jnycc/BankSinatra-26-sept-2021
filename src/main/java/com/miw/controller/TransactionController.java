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

    @PostMapping("/buy") //TODO: URL aanpassen zeer waarschijnlijk
    public ResponseEntity<?> doTransaction(@RequestBody String transactionAsJson){

        Transaction transaction = gson.fromJson(transactionAsJson, Transaction.class);
        transaction = transactionService.setTransactionPrice(transaction);

        //TODO: dit even voor overzichtelijkheid gedaan, maar weghalen waarschijnlijk
        int seller = transaction.getSeller();
        int buyer = transaction.getBuyer();
        Crypto crypto = transaction.getCrypto();
        double units = transaction.getUnits();
        double transactionPrice = transaction.getTransactionPrice();
        double bankCosts = transaction.getBankCosts();

        if(!transactionService.checkSufficientCrypto(seller, crypto, units)){
            return new ResponseEntity<>("Seller has insufficient assets. Transaction cannot be completed.", HttpStatus.CONFLICT);
        } else if(!transactionService.checkSufficientBalance(seller, buyer, transactionPrice, bankCosts)){
            return new ResponseEntity<>("Buyer has insufficient funds. Transaction cannot be completed.", HttpStatus.CONFLICT);
        }

        transactionService.transferBalance(seller, buyer, transactionPrice);
        transactionService.transferCrypto(seller, buyer, crypto, units);
        //transactionService.transferBankCosts(seller, buyer, transactionPrice, bankCosts);

        transactionService.registerTransaction(transaction);
        return new ResponseEntity<>("Joepie de poepie, transactie gedaan", HttpStatus.OK);
    }

}
