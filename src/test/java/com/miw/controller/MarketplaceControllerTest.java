package com.miw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.miw.database.RootRepository;
import com.miw.model.Bank;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.*;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.thymeleaf.spring5.expression.Mvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarketplaceController.class)
class MarketplaceControllerTest {

    private Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);
    private final String bankName = "Bank Sinatra";
    private final int bankSinatraAccountID = Bank.BANK_ID;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RootRepository repository;
    @MockBean
    private Bank bank;

    @Test
    void getNameByAccountID() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/requestName").contentType("application/json")
                .content(objectMapper.writeValueAsString(bankSinatraAccountID)))
                .andExpect(status().isOk()).andReturn();
        String expectedResponse = bankName;
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
    }


}