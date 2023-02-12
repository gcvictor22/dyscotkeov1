package com.salesianostriana.dam.dyscotkeov1.validation.annotation;

import com.salesianostriana.dam.dyscotkeov1.validation.validator.UniquePhoneNumberValidator;
import com.salesianostriana.dam.dyscotkeov1.validation.validator.UniqueUserNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Documented
public @interface UniquePhoneNumber {

    String message() default "El numero de teléfono ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


