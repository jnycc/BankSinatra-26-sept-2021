package com.miw.controller;

import com.miw.model.Client;
import com.miw.service.authentication.HashService;
import com.miw.service.RegistrationService;
import com.miw.service.authentication.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RegisterController {

    private RegistrationService registrationService;
    private ValidationService validationService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(RegistrationService registrationService, ValidationService validationService, HashService hashService){
        super();
        this.registrationService = registrationService;
        this.validationService = validationService;
        this.hashService = hashService;
        logger.info("New RegisterController created");
    }


    @PutMapping("/register") //TODO: RequestParam in RequestBody veranderen
    public ResponseEntity<?> registerUser(@RequestBody Client client){
        //Check volledigheid en juiste format vereiste gegevens (ValidationService)
//        StringBuilder errors = new StringBuilder();
//        errors.append(validationService.validateInput());
//        if(errors.length() != 0) {
//            return ResponseEntity.badRequest().body("Incomplete or incorrect fields: " + errors.toString());
//        }
/*        Address address = new Address(city, zipCode, street, houseNumber, houseNrAddition);
        User potentialUser = new Client(email, password, firstName, prefix, lastName, birthdate, BSN, address);
        String invalidFields = validationService.validateInput(potentialUser);
        if (!invalidFields.isEmpty()) {
            return ResponseEntity.badRequest().body("Registration failed. Incomplete or incorrect fields: " + invalidFields);
        }*/
        //Check if existing user (ValidationService of UserService)
        if (validationService.checkExistingAccount(client.getEmail())) {
            return ResponseEntity.badRequest().body("Registration failed. Account already exists.");
        }
        // Gebruiker opslaan in database en beginkapitaal toewijzen. Succesmelding geven.
        client = hashService.hash(client);
        registrationService.register(client);
        //return new ResponseEntity<>("User successfully registered." + user, HttpStatus.CREATED);
        return new ResponseEntity<>("User successfully registered.", HttpStatus.CREATED);

        //return ResponseEntity.ok("User successfully registered." + user);
    }
}
