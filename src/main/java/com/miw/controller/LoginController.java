package com.miw.controller;

import com.miw.service.UserService;
import com.miw.service.authentication.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private UserService userService;
    private HashService hashService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

}