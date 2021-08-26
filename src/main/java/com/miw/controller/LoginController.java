package com.miw.controller;

import com.miw.model.Credentials;
import com.miw.model.User;
import com.miw.service.authentication.AuthenticationService;
import com.miw.service.authentication.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private AuthenticationService authenticationService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(AuthenticationService authenticationService, HashService hashService) {
        super();
        this.authenticationService = authenticationService;
        this.hashService = hashService;
    }

    // TODO User logt uit ? -> expire token
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Credentials credentials) {
        //TODO: check validity input
        String token = authenticationService.authenticate(credentials);
        if (!token.isEmpty()) {
            return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
