package com.miw.controller;

import com.google.gson.Gson;
import com.miw.model.Client;
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

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(TransactionService transactionService){
        super();
        this.transactionService = transactionService;
        logger.info("New TransactionController-object created");
    }

    @PostMapping("/buy") //TODO: URL aanpassen zeer waarschijnlijk
    public ResponseEntity<?> doTransaction(@RequestBody String dingetjeHernoemMij){
        Gson gson = new Gson();
        Transaction transaction = gson.fromJson(dingetjeHernoemMij, Transaction.class); //TODO: met team Frontend afstemmen hoe dit precies binnen gaat komen
        Client seller = transaction.getSeller();
        Client buyer = transaction.getBuyer();
        Crypto crypto = transaction.getCrypto();
        double units = transaction.getUnits();
        double price = transaction.getPrice();
        double bankCosts = transaction.getBankCosts();

        if(!transactionService.checkSufficientCrypto(seller, crypto, units)){
            return new ResponseEntity<>("Seller has insufficient assets. Transaction cannot be completed.", HttpStatus.CONFLICT);
        } else if(!transactionService.checkSufficientBalance(buyer.getEmail(), price)){
            return new ResponseEntity<>("Buyer has insufficient funds. Transaction cannot be completed.", HttpStatus.CONFLICT);
        }

        transactionService.transferBalance(seller, buyer, price, bankCosts);
        transactionService.transferCrypto(seller, buyer, crypto, units);


        return new ResponseEntity<>("Joepie de poepie, transactie gedaan", HttpStatus.OK);
    }

}
