package com.miw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miw.database.JdbcAdminDao;
import com.miw.database.JdbcClientDao;
import com.miw.database.JdbcTransactionDao;
import com.miw.database.JdbcUserDao;
import com.miw.model.Administrator;
import com.miw.model.Client;
import com.miw.model.User;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private JdbcTransactionDao jdbcTransactionDao;
    private JdbcUserDao jdbcUserDao;
    private JdbcClientDao jdbcClientDao;
    private JdbcAdminDao jdbcAdminDao;

    @Autowired
    public AdminController(JdbcTransactionDao jdbcTransactionDao, JdbcUserDao jdbcUserDao, JdbcClientDao jdbcClientDao, JdbcAdminDao jdbcAdminDao) {
        super();
        this.jdbcTransactionDao = jdbcTransactionDao;
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcClientDao = jdbcClientDao;
        this.jdbcAdminDao = jdbcAdminDao;
        logger.info("New AdminController created");
    }

    @PutMapping("/admin/updateFee")
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

    @GetMapping("/admin/getBankFee")
    public ResponseEntity<?> getBankFee(@RequestHeader("Authorization") String token) {
        if (TokenService.validateAdmin(token)) {
            return ResponseEntity.ok(jdbcTransactionDao.getBankCosts());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/admin/getUserData")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String token, @RequestParam String email) {
        User user = jdbcUserDao.getUserByEmail(email);

        if (user instanceof Client) {
            user = jdbcClientDao.findByEmail(user.getEmail());
        } else if (user instanceof Administrator) {
            user = jdbcAdminDao.findByEmail(user.getEmail());
        }

        user.setSalt(null); user.setPassword(null); // remove data that don't need to be shown on the front-end
        if (TokenService.validateAdmin(token)) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/admin/toggleBlock")
    public ResponseEntity<?> toggleBlock(@RequestHeader("Authorization") String token, @RequestParam String email) {
        User user = jdbcUserDao.getUserByEmail(email);
        if (TokenService.validateAdmin(token)) {
            jdbcUserDao.toggleBlock(!user.isBlocked(), user.getUserId()); // block toggle through inversion of initial block status
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
