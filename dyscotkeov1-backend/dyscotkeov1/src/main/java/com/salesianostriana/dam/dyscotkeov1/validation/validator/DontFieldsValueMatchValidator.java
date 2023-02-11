package com.salesianostriana.dam.dyscotkeov1.validation.validator;

import com.salesianostriana.dam.dyscotkeov1.validation.annotation.DontFieldsValueMatch;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DontFieldsValueMatchValidator implements ConstraintValidator<DontFieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(DontFieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {

        Object fieldValue = PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(field);
        Object fieldMatchValue = PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(fieldMatch);

        if (fieldValue != null) {
            return !fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
    }

}
