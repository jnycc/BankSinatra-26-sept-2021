package com.miw.controller;

import com.miw.model.Client;
import com.miw.service.authentication.HashService;
import com.miw.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;


@RestController
@RequestMapping("/register")
public class RegisterController {

    private RegistrationService registrationService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(RegistrationService registrationService, HashService hashService){
        super();
        this.registrationService = registrationService;
        this.hashService = hashService;
        logger.info("New RegisterController-object created");
    }


    @PostMapping
    public ResponseEntity<?> registerClient(@Valid @RequestBody Client client){
        //Validatie volledigheid en juiste format van input zijn in de domeinklassen zelf gebouwd.
        //Check of klant al bestaat in de database.
        if (registrationService.checkExistingAccount(client.getEmail())) {
            return new ResponseEntity<>("Registration failed. Account already exists.", HttpStatus.CONFLICT);
        }
        // Gebruiker opslaan in database en beginkapitaal toewijzen. Succesmelding geven.
        client = hashService.hash(client);
        registrationService.register(client);
        return new ResponseEntity<>("User successfully registered. Welcome to Bank Sinatra!", HttpStatus.CREATED);
    }
}
