package com.salesianostriana.dam.dyscotkeov1.validation.validator;

import com.salesianostriana.dam.dyscotkeov1.user.service.UserService;
import com.salesianostriana.dam.dyscotkeov1.validation.annotation.UniqueUserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.hasText(s) && !userService.existsByUserName(s);
    }
}
