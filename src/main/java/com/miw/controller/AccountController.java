package com.miw.controller;

import com.miw.database.JdbcTransactionDao;
import com.miw.service.authentication.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



public class AccountController {

    private JdbcTransactionDao jdbcTransactionDao;

    public AccountController(JdbcTransactionDao jdbcTransactionDao) {
        this.jdbcTransactionDao = jdbcTransactionDao;
    }

    @PostMapping("/getTransactions")
    public ResponseEntity<?> getTransactions(@RequestBody String token) {
        int ID = TokenService.getValidUserID(token);
        return (ResponseEntity<?>) jdbcTransactionDao.getTransactionsByUserId(ID);
    }


    //TODO: endPoint getBalance hiernaar verplaatsen





}
