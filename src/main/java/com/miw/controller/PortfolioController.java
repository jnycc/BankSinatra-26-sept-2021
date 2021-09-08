/**
 * @Author: Johnny Chan
 * @Description: Controller which validates the received JWT and obtains and returns the client's portfolio
 * with current, historical and delta values.
 */
package com.miw.controller;

import com.google.gson.Gson;
import com.miw.service.PortfolioService;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PortfolioController {

    private PortfolioService portfolioService;
    private Gson gson;
    private final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    public PortfolioController(PortfolioService portfolioService, Gson gson) {
        this.portfolioService = portfolioService;
        this.gson = gson;
    }

    @GetMapping("/portfolio")
    public ResponseEntity<?> getPortfolioOverview(@RequestHeader("Authorization") String token) {
        System.out.println("Test, token vooraf is: " + token);
        int userId = TokenService.getValidUserID(token);
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //PortfolioService aanroepen om de vereiste gegevens te verzamelen en returnen aan frontend
        Map<String, Object> portfolio = portfolioService.getPortfolio(userId);
        return ResponseEntity.ok(portfolio);
    }
}
