package com.miw.service;

import com.miw.database.JdbcTransactionDao;
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

    public boolean checkSufficientBalance(Client buyer, double transactionPrice){
        // TODO: aanvullen als AccountDao is aangevuld
        //maar basically rootRepository.findAccountByClient(buyer).getBalance >= transactionPrice
        return false;
    }

    public boolean checkSufficientCrypto(Client seller, Crypto crypto, double units){
        // TODO: aanvullen als we PortfolioDao oid hebben
        //maar ook hier: aantal crypto in portfolio seller moet >= aantal crypto dat de buyer wil kopen
        return false;
    }

}
