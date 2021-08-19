package com.miw.controller;

import com.miw.database.domain.User;
import com.miw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {

    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService){
        super();
        this.userService = userService;
        logger.info("New UserController");
    }

    @PutMapping("/register") //TODO: evt. deze URL aanpassen en RequestParam wellicht ook
    public User registerUser(@RequestParam String username){
        User user = userService.register(username);
        return user;
    }
}
