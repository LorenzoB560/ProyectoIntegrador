package org.grupob.empapp.validation.email;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidadoValidator implements ConstraintValidator<EmailValidado, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
    }

