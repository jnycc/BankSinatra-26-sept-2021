package com.miw.controller;

import com.miw.model.Address;
import com.miw.model.Client;
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
public class RegisterController {

    private UserService userService;
    private ValidationService validationService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(UserService userService, HashService hashService){
        super();
        this.userService = userService;
        this.validationService = validationService;
        this.hashService = hashService;
        logger.info("New RegisterController created");
    }

/*    @PutMapping("/register") //TODO: evt. deze URL aanpassen en RequestParam wellicht ook
    public User registerUser(@RequestParam String username,
                             @RequestParam String password){
        User user = userService.register(username, hashService.hash(password));
        return user;
    }*/

    @PutMapping("/register") //TODO: evt. deze URL aanpassen en RequestParam wellicht ook
    public ResponseEntity<?> registerUser(@RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String firstName,
                                       @RequestParam(required = false) String prefix,
                                       @RequestParam String lastName,
                                       @RequestParam Date birthdate,
                                       @RequestParam int BSN,
                                       @RequestParam String city,
                                       @RequestParam String street,
                                       @RequestParam int houseNumber,
                                       @RequestParam String zipCode,
                                       @RequestParam(required = false) String houseNrAddition){
        //Check volledigheid en juiste format vereiste gegevens (ValidationService)
//        StringBuilder errors = new StringBuilder();
//        errors.append(validationService.validateInput());
//        if(errors.length() != 0) {
//            return ResponseEntity.badRequest().body("Incomplete or incorrect fields: " + errors.toString());
//        }
        Address address = new Address(city, zipCode, street, houseNumber, houseNrAddition);
        User potentialUser = new Client(email, password, firstName, prefix, lastName, birthdate, BSN, address);
        String invalidFields = validationService.validateInput(potentialUser);
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
