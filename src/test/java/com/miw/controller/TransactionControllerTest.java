package com.miw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miw.model.Crypto;
import com.miw.model.Transaction;
import com.miw.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    public TransactionControllerTest(MockMvc mockMvc){
        super();
        this.mockMvc = mockMvc;
    }

    private Transaction testTransaction;
    private Crypto testCrypto;

    @BeforeEach
    public void setup(){
        testCrypto = new Crypto("TestCrypto", "TCR", "It's cryptocurrency just for testing!", 500.00);
        testTransaction = new Transaction(1, 2, testCrypto, 3);
        testTransaction.setBankCosts(0.02);
    }


    @Test
    void validTransactionTest(){
        Mockito.when(transactionService.checkSufficientCrypto(testTransaction.getSeller(),
                testTransaction.getCrypto(), testTransaction.getUnits())).thenReturn(true);
        Mockito.when(transactionService.checkSufficientBalance(testTransaction.getSeller(),
                testTransaction.getBuyer(), testTransaction.getTransactionPrice(), testTransaction.getBankCosts()))
                .thenReturn(true);

        try {
            MockHttpServletResponse response =
                    mockMvc.perform(post("/buy")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(testTransaction)))
                            .andExpect(status().isOk())
                            .andDo(print()).andReturn().getResponse();
            assertThat(response.getContentType()).isEqualTo("text/plain;charset=UTF-8");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void negativeCryptoTest(){
        Transaction testTransaction2 = new Transaction(1, 2, testCrypto, -3);
        try {
            mockMvc.perform(post("/buy")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testTransaction2)))
                    .andExpect(status().isConflict())
                    .andDo(print());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void insufficientCryptoTest(){
        Mockito.when(transactionService.checkSufficientCrypto(testTransaction.getSeller(),
                testTransaction.getCrypto(), testTransaction.getUnits())).thenReturn(false);

        try {
            mockMvc.perform(post("/buy")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testTransaction)))
                    .andExpect(status().isConflict())
                    .andDo(print());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void insufficientBalanceTest(){
        Mockito.when(transactionService.checkSufficientCrypto(testTransaction.getSeller(),
                testTransaction.getCrypto(), testTransaction.getUnits())).thenReturn(true);
        Mockito.when(transactionService.checkSufficientBalance(testTransaction.getSeller(),
                testTransaction.getBuyer(), testTransaction.getTransactionPrice(),
                testTransaction.getBankCosts())).thenReturn(false);

        try {
            mockMvc.perform(post("/buy")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testTransaction)))
                    .andExpect(status().isConflict())
                    .andDo(print());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }





}