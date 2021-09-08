package com.miw.controller;

import com.miw.service.authentication.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class IndexController {

    @GetMapping("/")
    String index(){
        return "index";
    }

    // Validates if user is a client. Use before endpoints to grand access to right html pages.
    public ResponseEntity<?> validateClient(@RequestHeader("Authorization") String token) {
        if (TokenService.validateClient(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Validates if user is an admin. Use before endpoints to grand access to right html pages.
    public ResponseEntity<?> validateAdmin(@RequestHeader("Authorization") String token) {
        if (TokenService.validateAdmin(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Validates if user is legitimate and logged in. Use before endpoints to grand access to right html pages.
    public ResponseEntity<?> validateUser(@RequestHeader("Authorization") String token) {
        if (TokenService.validateJWT(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
