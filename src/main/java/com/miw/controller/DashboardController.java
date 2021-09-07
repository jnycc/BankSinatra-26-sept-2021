package com.miw.controller;

import com.miw.database.Dao;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private TokenService tokenService;

    @Autowired
    public DashboardController() {
        super();
        logger.info("New DashboardController created");
    }

    @GetMapping("/dashboard")
    void something(){
        logger.info("something");
    }
}
