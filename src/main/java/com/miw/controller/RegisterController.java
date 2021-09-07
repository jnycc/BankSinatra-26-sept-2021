/**
 * @Author: Johnny Chan en Renée Jansen.
 * Deze class mapt de /register endpoints voor nieuwe klanten en admins.
 */
package com.miw.controller;

import com.google.gson.*;
import com.miw.model.Administrator;
import com.miw.model.Client;
import com.miw.service.authentication.HashService;
import com.miw.service.authentication.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.time.LocalDate;


@RestController
@Validated
public class RegisterController {

    private RegistrationService registrationService;
    private HashService hashService;
    private Gson gson;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(RegistrationService registrationService, HashService hashService) {
        super();
        this.registrationService = registrationService;
        this.hashService = hashService;
        logger.info("New RegisterController-object created");
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString());
            }
        }).create();
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody String client) {
        //Validatie volledigheid en juiste format van input zijn in de domeinklassen zelf gebouwd.
        //Check of klant al bestaat in de database.
        @Valid Client client1 = gson.fromJson(client, Client.class);

        if (registrationService.checkExistingClientAccount(client1.getEmail())) {
            return new ResponseEntity<>("Registration failed. Account already exists.", HttpStatus.CONFLICT);
        }
        //Gebruiker opslaan in database en beginkapitaal toewijzen. Succesmelding geven.
        client1 = (Client) hashService.hash(client1);
        registrationService.register(client1);
        return new ResponseEntity<>("User successfully registered. Welcome to Bank Sinatra!", HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody String admin) {
        //Check of admin-account reeds bestaat in de database.
        @Valid Administrator admin1 = gson.fromJson(admin, Administrator.class);

        if (registrationService.checkExistingClientAccount(admin1.getEmail())) {
            return new ResponseEntity<>("Registration failed. Admin account already exists.", HttpStatus.CONFLICT);
        }
        //Admin opslaan in de database met een blocked status.
        admin1 = (Administrator) hashService.hash(admin1);
        registrationService.register(admin1);
        return new ResponseEntity<>("Your request for an administrator account has been received and is pending " +
                "further approval. \nFor inquiries, please contact your Manager or IT Supervisor.", HttpStatus.CREATED);
    }

}
