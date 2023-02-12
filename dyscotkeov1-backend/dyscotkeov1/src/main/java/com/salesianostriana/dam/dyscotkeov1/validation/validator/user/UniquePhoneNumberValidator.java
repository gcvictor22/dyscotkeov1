package com.salesianostriana.dam.dyscotkeov1.validation.validator.user;

import com.salesianostriana.dam.dyscotkeov1.user.service.UserService;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.user.UniquePhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String p, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(p) && !userService.existsByPhoneNumber(p);
    }
}
