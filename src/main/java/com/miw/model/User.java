package com.miw.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO Make abstract, create extension classes Client and Administrator
public class User {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    // willen we user ids?

    private String emailaddress;
    private String password;
    private String name;
    private String prefix;
    private String surname;
    // hoort de salt in de user model?
    private String salt;
    private boolean isBlocked;


    public User(String emailaddress, String password, String name, String prefix, String surname, String salt, boolean isBlocked) {
        this.emailaddress = emailaddress;
        this.password = password;
        this.name = name;
        this.prefix = prefix;
        this.surname = surname;
        this.salt = salt;
        this.isBlocked = isBlocked;
        logger.info("new user");
    }

    public User(String emailaddress, String password) {
        super();
        this.emailaddress = emailaddress;
        this.password = password;
        logger.info("new user");
    }

    public User() {
        this(null, null, null, null, null, null, false);
    }

    public User(String emailaddress, String password, String name, String prefix, String surname) {
        this(emailaddress, password, name, prefix, surname, null, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getPassword() {
        return password;
    }
}
