package com.miw.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< Updated upstream
import javax.validation.constraints.*;

// TODO Make abstract, create extension classes Client and Administrator
=======
// TODO access modifiers aanpassen naar protected
>>>>>>> Stashed changes
public abstract class User {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    private int userId;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size (min = 8, max = 64)
    private String password;

    private String salt;

    @NotEmpty
    private String firstName;

    private String prefix;

    @NotEmpty
    private String lastName;

    private boolean isBlocked;


    public User(String email, String password, String salt, String firstName, String prefix, String lastName, boolean isBlocked) {
        this.userId = generateUserid();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.prefix = prefix;
        this.lastName = lastName;
        this.salt = salt;           //
        this.isBlocked = isBlocked;
        logger.info("new User created");
    }

    public User(String email, String password) {
        super();
        this.email = email;
        this.password = password;
        logger.info("new User created");
    }

    public User() {
        this(null, null, null, null, null, null, false);
    }

    public User(String email, String password, String firstName, String prefix, String lastName) {
        this(email, password, firstName, prefix, lastName, null, false);
    }

    public User(String email, String firstName, String prefix, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.prefix = prefix;
        this.lastName = lastName;
    }

    private int generateUserid() {
        // TODO genereer userId
        return 0;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

<<<<<<< Updated upstream
    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


=======
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
>>>>>>> Stashed changes
}
