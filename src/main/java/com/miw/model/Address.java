package com.miw.model;

public class Address {

    private String city;
    private String zipCode;
    private String street;
    private int houseNumber;
    private String houseNumberExtension;


    public Address(String city, String zipCode, String street, int houseNumber, String houseNumberExtension) {
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseNumberExtension = houseNumberExtension;

    }
}

