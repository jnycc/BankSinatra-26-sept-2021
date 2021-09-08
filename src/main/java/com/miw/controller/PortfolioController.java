package com.miw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miw.model.Client;
import com.miw.service.PortfolioService;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> getPortfolioOverview(@RequestHeader("Authorization") String token
                                                  //@RequestBody int userId
                                                  ) {
        System.out.println("Test, token vooraf is: " + token);
        int userId = TokenService.getUserID(token);
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
//        JsonObject jsonObject = gson.fromJson(emailAsJson, JsonObject.class); //verander de json in een json-object
//        String email = jsonObject.get("email").getAsString(); //haal o.b.v. key de value uit json-object
        //System.out.printf("Json:\n%s\nConverted string: %s\n", emailAsJson, email);
        //Eerst userId vinden van de client
//        Client client = portfolioService.findClientByEmail(email);
        //PortfolioService aanroepen om de vereiste gegevens te verzamelen en returnen aan frontend
        Map<String, Object> portfolio = portfolioService.getPortfolio(userId);
        return ResponseEntity.ok(portfolio);
    }
}
