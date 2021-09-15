package com.miw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miw.database.JdbcAccountDao;
import com.miw.database.JdbcAssetDao;
import com.miw.database.JdbcTransactionDao;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private JdbcTransactionDao jdbcTransactionDao;

    @Autowired
    public AdminController(JdbcTransactionDao jdbcTransactionDao) {
        super();
        this.jdbcTransactionDao = jdbcTransactionDao;
        logger.info("New AdminController created");
    }

    @PutMapping("/updateFee")
    public ResponseEntity<?> updateFee(@RequestBody String json) {
        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
        String token = convertedObject.get("token").getAsString();
        double fee = convertedObject.get("fee").getAsDouble();
        if (TokenService.validateAdmin(token)) {
            jdbcTransactionDao.updateBankCosts(fee);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // TODO: is dit de juiste http code?
    }

    @PutMapping("/blockUnblock")
    public ResponseEntity<?> blockUnblock(@RequestBody String json) {
        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
        String token = convertedObject.get("token").getAsString();
        int userID = convertedObject.get("id").getAsInt();
        boolean blocked = convertedObject.get("blocked").getAsBoolean();
        if (TokenService.validateAdmin(token)) {
            if (blocked)
            jdbcUserDao.blockUser(userID);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // TODO: is dit de juiste http code?
    }
}
