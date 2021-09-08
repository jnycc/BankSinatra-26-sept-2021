package com.miw.service;

import com.miw.database.RootRepository;
import com.miw.model.Account;
import com.miw.model.Bank;
import com.miw.model.Crypto;
import com.miw.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    RootRepository mockRepo = Mockito.mock(RootRepository.class);
    TransactionService transactionService = new TransactionService(mockRepo);

    Account testSellerAccount;
    Account testBuyerAccount;
    Bank testBank;
    Crypto testCrypto;
    Transaction testTransaction;

    public TransactionServiceTest(){
        super();
    }

    @BeforeEach
    public void setup(){
        testBank = Bank.getBankSinatra();
        testBank.getAccount().setAccountId(0);
        testSellerAccount = new Account();
        testSellerAccount.setAccountId(1);
        testBuyerAccount = new Account();
        testBuyerAccount.setAccountId(2);

        testCrypto = new Crypto("TestCrypto", "TCR", "It's cryptocurrency just for testing!", 500.00);
        testTransaction = new Transaction(1, 2, testCrypto, 3);
        testTransaction.setBankCosts(0.02);

        Mockito.when(mockRepo.getAccountById(0)).thenReturn(testBank.getAccount());
        Mockito.when(mockRepo.getAccountById(1)).thenReturn(testSellerAccount);
        Mockito.when(mockRepo.getAccountById(2)).thenReturn(testBuyerAccount);
    }

    @Test
    void setTransactionPriceTest(){
        double expected = 1500.00;
        double actual = transactionService.setTransactionPrice(testTransaction).getTransactionPrice();
        assertEquals(expected, actual);
    }

    @Test
    void checkSufficientBalance1(){
        assertTrue(transactionService.checkSufficientBalance(1, 2, testTransaction.getTransactionPrice(), testTransaction.getBankCosts()));
    }

    @Test
    void checkSufficientBalance2(){
        assertTrue(transactionService.checkSufficientBalance(1, 0, testBank.getAccount().getBalance() - 1, testTransaction.getBankCosts()));
    }

    @Test
    void checkInsufficientBalance(){
        assertFalse(transactionService.checkSufficientBalance(1, 2, 1000000, testTransaction.getBankCosts()));
    }

    @Test
    void checkInsufficientBalanceForBankCosts(){
        assertFalse(transactionService.checkSufficientBalance(0, 1, testBuyerAccount.getBalance() - 1, testTransaction.getBankCosts()));
    }

}