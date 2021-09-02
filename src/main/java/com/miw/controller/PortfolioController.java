package com.miw.controller;

import com.miw.service.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class PortfolioController {
    private PortfolioService portfolioService;

    private final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/portfolio")
    public ResponseEntity<?> getPortfolioOverview(@RequestHeader("Authorization") String token,
                                                  @RequestBody String email) {// Moet binnenkrijgen wie het is
        //PortfolioService aanroepen om de vereiste gegevens te verzamelen en returnen

        return null;

    }
}
