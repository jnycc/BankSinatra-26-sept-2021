package com.miw.model;

public class Cryptocoin {

    // ATTRIBUTES
    private String name;
    private String initials;
    private String description;
    private double value;


    // CONSTRUCTORS
    public Cryptocoin(String name, String initials, String description) {
        this.name = name;
        this.initials = initials;
        this.description = description;
        this.value = retrieveValue();
    }



    // METHODS
    private double retrieveValue() {
        //TODO: get recent value of cryptocoin thru API?
        return 0.0;
    }

    // GETTERS & SETTERS

}
