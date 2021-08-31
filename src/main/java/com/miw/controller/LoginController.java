package com.miw.controller;

import com.miw.database.JdbcClientDao;
import com.miw.model.Credentials;
import com.miw.service.authentication.AuthenticationService;
import com.miw.service.authentication.TokenService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@Validated
public class LoginController {

    private AuthenticationService authenticationService;
    private TokenService tokenService;
    private JdbcClientDao jdbcClientDao;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthenticationService authenticationService, TokenService ts, JdbcClientDao jdbcClientDao) {
        super();
        this.authenticationService = authenticationService;
        this.tokenService = ts;
        this.jdbcClientDao = jdbcClientDao;
        logger.info("New LoginController Created");
    }

    // TODO User logt uit ? -> expire token
    // TODO Check of de gebruiker al een geldige token bezit
    // TODO Eventueel JWT implementeren
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody Credentials credentials) {
        String token = authenticationService.authenticate(credentials);
        if (!token.isEmpty()) {
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/gegevens/{email}")
    public ResponseEntity<?> showMyData(@RequestHeader("Authorization") String token, @PathVariable("email") @Email String email) {
        //Claims claims = tokenService.decodeJwt

        if (tokenService.decodeJWTBool(token)) {
            return ResponseEntity.ok(jdbcClientDao.findByEmail(email));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
