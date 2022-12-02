package com.cramin.springbootdemo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class SqlValidator implements ConstraintValidator<SqlValidate, String> {

    private String[] invalid;

    @Override
    public void initialize(SqlValidate constraintAnnotation) {
        invalid = Optional.ofNullable(constraintAnnotation.invalid()).orElse(new String[]{});
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String upper = s.toUpperCase();
        for (String in : invalid) {
            if (upper.contains(in)) return false;
        }
        return true;
    }
}
