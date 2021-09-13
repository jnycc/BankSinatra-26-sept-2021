package com.miw.controller;

import com.google.gson.*;
import com.miw.model.Crypto;
import com.miw.model.Transaction;
import com.miw.service.TransactionService;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class TransactionController {

    private TransactionService transactionService;
    private Gson gson;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(TransactionService transactionService){
        super();
        this.transactionService = transactionService;
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString());
            }
        }).create();
        logger.info("New TransactionController-object created");
    }

    //TODO: Authorization zo instellen dat een ingelogde gebruiker niet als een andere gebruiker kan kopen
    //TODO: Methode verder opschonen
    @PostMapping("/buy") //TODO: URL aanpassen naar marketplace?
    public ResponseEntity<?> doTransaction(@RequestHeader("Authorization") String token, @RequestBody String transactionAsJson){
        if (!TokenService.validateJWT(token)) {
            return new ResponseEntity<>("Invalid login credentials, try again", HttpStatus.UNAUTHORIZED);
        }

        Transaction transaction = gson.fromJson(transactionAsJson, Transaction.class);
        int userId = TokenService.getValidUserID(token);

        if(userId != transaction.getBuyer()){
            return new ResponseEntity<>("You are not authorized to purchase assets for another client," +
                    " stop it", HttpStatus.CONFLICT); //TODO: is dit de juiste statuscode?
        }

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

    @GetMapping("/cryptos")
    public ResponseEntity<?> getCryptoOverview(@RequestHeader("Authorization") String token) {
        if (!TokenService.validateJWT(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //TODO: Map in Crypto toevoegen met prijsdeltas
        List<Crypto> cryptoOverview = transactionService.getCryptoOverview();
        return ResponseEntity.ok(cryptoOverview);
    }
}
