package com.miw.controller;

import com.miw.service.authentication.HashService;
import com.miw.model.User;
import com.miw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RegisterController {

    private UserService userService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(UserService userService, HashService hashService){
        super();
        this.userService = userService;
        this.hashService = hashService;
        logger.info("New RegisterController created");
    }

    @PutMapping("/register") //TODO: evt. deze URL aanpassen en RequestParam wellicht ook
    public User registerUser(@RequestParam String username,
                             @RequestParam String password){
        User user = userService.register(username, hashService.hash(password));
        return user;
    }
}
