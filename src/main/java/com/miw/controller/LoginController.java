package com.miw.controller;

import com.miw.database.JdbcUserDao;
import com.miw.model.Credentials;
import com.miw.service.authentication.AuthenticationService;
import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private AuthenticationService authenticationService;
    private TokenService tokenService;
    private JdbcUserDao jdbcUserDao;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthenticationService authenticationService, TokenService ts, JdbcUserDao jdbcUserDao) {
        super();
        this.authenticationService = authenticationService;
        this.tokenService = ts;
        this.jdbcUserDao = jdbcUserDao;
        logger.info("New LoginController Created");
    }

    // TODO User logt uit ? -> expire token
    // TODO Check of de gebruiker al een geldige token bezit
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Credentials credentials) {
        //TODO: check validity input
        String token = authenticationService.authenticate(credentials);
        if (!token.isEmpty()) {
            return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/gegevens/{email}")
    public ResponseEntity<?> toonMijnGegevens(@RequestHeader("Authorization") String token, @PathVariable("email") String email) {
        if (tokenService.validateToken(token)) {
            return ResponseEntity.ok(jdbcUserDao.findByEmail(email));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
