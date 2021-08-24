package com.miw.controller;

import com.miw.service.authentication.HashService;
import com.miw.model.User;
import com.miw.service.UserService;
import com.miw.service.authentication.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@RestController
public class UserController {

    private UserService userService;
    private ValidationService validationService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, ValidationService validationService, HashService hashService){
        super();
        this.userService = userService;
        this.validationService = validationService;
        this.hashService = hashService;

        logger.info("New UserController created");
    }

    @PutMapping("/register") //TODO: evt. deze URL aanpassen en RequestParam wellicht ook
    public ResponseEntity<?> registerUser(@RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String firstName,
                                       @RequestParam(required = false) String prefix,
                                       @RequestParam String lastName,
                                       @RequestParam Date birthdate,
                                       @RequestParam int BSN,
                                       @RequestParam String street,
                                       @RequestParam String houseNr,
                                       @RequestParam String zipCode,
                                       @RequestParam(required = false) String houseNrAddition){
        //Check volledigheid en juiste format vereiste gegevens (ValidationService)
//        StringBuilder errors = new StringBuilder();
//        errors.append(validationService.validateInput());
//        if(errors.length() != 0) {
//            return ResponseEntity.badRequest().body("Incomplete or incorrect fields: " + errors.toString());
//        }
        User potentialUser = new User(email, password, firstName, prefix, lastName, birthdate, BSN, new Address(street, houseNr, zipCode, houseNrAddition));
        String invalidFields = validationService.validateInput();
        if (!invalidFields.isEmpty()) {
            return ResponseEntity.badRequest().body("Registration failed. Incomplete or incorrect fields: " + invalidFields);
        }
        //Check if existing user (ValidationService of UserService)
        if (validationService.checkExistingAccount(email)) {
            return ResponseEntity.badRequest().body("Registration failed. Account already exists.");
        }
        // Gebruiker opslaan in database en beginkapitaal toewijzen. Succesmelding geven.
        User user = userService.register(email, hashService.hash(password));
        return new ResponseEntity<>("User successfully registered." + user, HttpStatus.CREATED);
        //return ResponseEntity.ok("User successfully registered." + user);
    }
}
