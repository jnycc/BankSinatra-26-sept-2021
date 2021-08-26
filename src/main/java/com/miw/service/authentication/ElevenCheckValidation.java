package com.miw.service.authentication;

import com.miw.model.Client;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ElevenCheckValidation implements ConstraintValidator<ElevenCheck, Client> {

    @Override
    public void initialize(ElevenCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Client client, ConstraintValidatorContext constraintValidatorContext) {
       //TODO: write 11 check here :)
        return false;
    }
}
