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

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
