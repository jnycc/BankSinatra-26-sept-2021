package com.miw.model;

public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public Credentials() {}

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
