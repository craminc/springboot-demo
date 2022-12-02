package com.cramin.springbootdemo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SqlValidator.class})
public @interface SqlValidate {

    String message() default "select sql can not contains [DELETE, UPDATE, INSERT, CREATE, DROP, ALTER, TRUNCATE, ;]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] invalid() default {"DELETE ", "UPDATE ", "INSERT ", "CREATE ", "DROP ", "ALTER ", "TRUNCATE ", ";"};
}
