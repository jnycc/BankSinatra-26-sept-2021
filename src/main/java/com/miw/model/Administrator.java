package com.miw.model;

public class Administrator extends User {

    public Administrator(String email, String password, String salt, String firstName, String prefix, String lastName) {
        super(email, password, salt, firstName, prefix, lastName, true);
    }

    public Administrator(String email, String password) {
        this(email, password, null, null, null, null);
    }

    //Default constructor die Spring gebruikt. Moet bij aanmaak nieuwe admin altijd blocked zijn in de database; in afwachting van goedkeuring.
    public Administrator() {
        this.isBlocked = true;
    }

    // METHODS


}
