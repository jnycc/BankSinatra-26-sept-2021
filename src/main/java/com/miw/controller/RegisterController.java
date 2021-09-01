/**
 * @Author: Johnny Chan en Renée Jansen.
 * Deze class mapt de /register endpoints voor nieuwe klanten en admins.
 */
package com.miw.controller;

import com.miw.model.Administrator;
import com.miw.model.Client;
import com.miw.service.authentication.HashService;
import com.miw.service.authentication.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;


@RestController
public class RegisterController {

    private RegistrationService registrationService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(RegistrationService registrationService, HashService hashService) {
        super();
        this.registrationService = registrationService;
        this.hashService = hashService;
        logger.info("New RegisterController-object created");
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@Valid @RequestBody Client client) {
        //Validatie volledigheid en juiste format van input zijn in de domeinklassen zelf gebouwd.
        //Check of klant al bestaat in de database.
        if (registrationService.checkExistingClientAccount(client.getEmail())) {
            return new ResponseEntity<>("Registration failed. Account already exists.", HttpStatus.CONFLICT);
        }
        //Gebruiker opslaan in database en beginkapitaal toewijzen. Succesmelding geven.
        client = (Client) hashService.hash(client);
        registrationService.register(client);
        return new ResponseEntity<>("User successfully registered. Welcome to Bank Sinatra!", HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody Administrator admin) {
        //Check of admin-account reeds bestaat in de database.
        if (registrationService.checkExistingClientAccount(admin.getEmail())) {
            return new ResponseEntity<>("Registration failed. Admin account already exists.", HttpStatus.CONFLICT);
        }
        //Admin opslaan in de database met een blocked status.
        admin = (Administrator) hashService.hash(admin);
        registrationService.register(admin);
        return new ResponseEntity<>("Your request for an administrator account has been received and is pending " +
                "further approval. \nFor inquiries, please contact your Manager or IT Supervisor.", HttpStatus.CREATED);
    }
}
