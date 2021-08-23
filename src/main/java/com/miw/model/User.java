package com.miw.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    // willen we user ids?

    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        super();
        this.username = username;
        this.password = password;
        this.role = role;
        logger.info("new user");
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        logger.info("new user");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
