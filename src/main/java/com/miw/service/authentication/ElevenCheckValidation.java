package com.miw.service.authentication;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.LinkedList;
import java.util.List;

public class ElevenCheckValidation implements ConstraintValidator<ElevenCheck, Integer> {

    @Override
    public void initialize(ElevenCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer bsn, ConstraintValidatorContext constraintValidatorContext) {
        int sum = elevenTestSum(makeDigits(bsn));
        return sum%11 == 0;
    }

    public List<Integer> makeDigits(Integer fullNr){
        List<Integer> digits = new LinkedList<>();

        //Dutch social security numbers with length 8 still exist
        //In order for them to pass the 11-test, they have a 0 added before their other numbers
        //TODO: tidy up this code
        if(fullNr.toString().length() == 8){
            digits.add(0, 0);
            while (fullNr > 0){
                digits.add(1, fullNr%10);
                fullNr = fullNr/10;
            }
        } else {
            while (fullNr > 0){
                digits.add(0, fullNr%10);
                fullNr = fullNr/10;
            }
        }
        return digits;
    }

    public int elevenTestSum(List<Integer> ints){
        int sum = 0;
        System.out.println("hoi");
        System.out.println(ints.size());
        for (int i = 0; i < ints.size() - 1; i++) {
            sum += ints.get(i) * (ints.size() - i);
        }
        sum += (ints.get(8) * -1); //TODO: tidy? This can probably be done inside for loop
        return sum;
    }

}
