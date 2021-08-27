package com.miw.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Credentials {
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 8, max = 64)
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
