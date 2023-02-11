package com.salesianostriana.dam.dyscotkeov1.validation.validator;

import com.salesianostriana.dam.dyscotkeov1.user.service.UserService;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.UniquePhoneNumber;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.UniqueUserName;
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
