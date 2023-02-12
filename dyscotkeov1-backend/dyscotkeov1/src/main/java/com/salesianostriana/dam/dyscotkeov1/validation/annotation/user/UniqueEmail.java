package com.salesianostriana.dam.dyscotkeov1.validation.annotation.user;

import com.salesianostriana.dam.dyscotkeov1.validation.validator.user.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "El email ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


