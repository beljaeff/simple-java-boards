package com.github.beljaeff.sjb.validator;

import com.nulabinc.zxcvbn.Zxcvbn;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//TODO: bring strength info to front
public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

    @Value("${password.min.strength}")
    private int strength;

    @Override
    public void initialize(PasswordStrength constraint) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if(password == null) {
            return  false;
        }
        return strength == 0 || new Zxcvbn().measure(password).getScore() >= strength;
    }
}