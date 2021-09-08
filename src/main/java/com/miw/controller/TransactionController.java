package com.miw.controller;

import com.google.gson.Gson;
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
        Transaction transaction = gson.fromJson(transactionAsJson, Transaction.class);

        if(transaction.getUnits() < 0){
            return new ResponseEntity<>("Buyer cannot purchase negative asssets. " +
                    "Transaction cannot be completed.", HttpStatus.CONFLICT);
        }

        transaction = setPrice(transaction);
        transaction = setCosts(transaction);

        if(!checkSufficientCrypto(transaction)){
            return new ResponseEntity<>("Seller has insufficient assets. Transaction cannot be completed.",
                    HttpStatus.CONFLICT);
        } else if(!checkSufficientBalance(transaction)){
            return new ResponseEntity<>("Buyer has insufficient funds. Transaction cannot be completed.",
                    HttpStatus.CONFLICT);
        }

        transfer(transaction);

        transactionService.registerTransaction(transaction);
        return new ResponseEntity<>("Joepie de poepie, transactie gedaan", HttpStatus.OK);
    }


    private void transfer(Transaction transaction){
        transactionService.transferBalance(transaction.getSeller(), transaction.getBuyer(),
                transaction.getTransactionPrice());
        transactionService.transferCrypto(transaction.getSeller(), transaction.getBuyer(),
                transaction.getCrypto(), transaction.getUnits());
        transactionService.transferBankCosts(transaction.getSeller(), transaction.getBuyer(),
                transaction.getTransactionPrice(), transaction.getBankCosts());
    }

    private Transaction setPrice(Transaction transaction){
        return transactionService.setTransactionPrice(transaction);
    }

    private Transaction setCosts(Transaction transaction){
        return transactionService.setBankCosts(transaction);
    }

    private boolean checkSufficientCrypto(Transaction transaction){
        try{
            return transactionService.checkSufficientCrypto(transaction.getSeller(), transaction.getCrypto(),
                    transaction.getUnits());
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkSufficientBalance(Transaction transaction){
        return transactionService.checkSufficientBalance(transaction.getSeller(), transaction.getBuyer(),
                transaction.getTransactionPrice(), transaction.getBankCosts());
    }
}
