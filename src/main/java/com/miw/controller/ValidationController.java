package com.miw.controller;

import com.miw.service.authentication.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ValidationController {
    private final Logger logger = LoggerFactory.getLogger(ValidationController.class);

    public ValidationController() {
        super();
        logger.info("New ValidationController-object created");
    }

    // Validates if user is a client. Use before endpoints to grand access to right html pages.
    @PostMapping
    public ResponseEntity<?> validateClient(@RequestHeader("Authorization") String token) {
        if (TokenService.validateClient(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // TODO: terug naar login paginga
        // TODO: terug naar pagina van voorheen
        // url opslaan in de body???
    }

    // Validates if user is an admin. Use before endpoints to grand access to right html pages.
    @PostMapping
    public ResponseEntity<?> validateAdmin(@RequestHeader("Authorization") String token) {
        if (TokenService.validateAdmin(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Validates if user is legitimate and logged in. Use before endpoints to grand access to right html pages.
    @PostMapping
    public ResponseEntity<?> validateUser(@RequestHeader("Authorization") String token) {
        if (TokenService.validateJWT(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
