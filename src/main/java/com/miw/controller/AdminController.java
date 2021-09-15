package com.miw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miw.database.JdbcTransactionDao;
import com.miw.database.JdbcUserDao;
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
    private JdbcUserDao jdbcUserDao;


    @Autowired
    public AdminController(JdbcTransactionDao jdbcTransactionDao, JdbcUserDao jdbcUserDao) {
        super();
        this.jdbcTransactionDao = jdbcTransactionDao;
        this.jdbcUserDao = jdbcUserDao;
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

    @PutMapping("/toggleBlock")
    public ResponseEntity<?> toggleBlock(@RequestBody String json) {
        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
        String token = convertedObject.get("token").getAsString();
        int userID = convertedObject.get("id").getAsInt();
        boolean initialBlockStatus = convertedObject.get("blocked").getAsBoolean(); // current block status of user, which we want to invert
        if (TokenService.validateAdmin(token)) {
            jdbcUserDao.toggleBlock(!initialBlockStatus, userID); // inversion happens here via the ! operator
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // TODO: is dit de juiste http code?
    }
}
