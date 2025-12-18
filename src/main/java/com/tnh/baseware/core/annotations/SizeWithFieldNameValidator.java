package com.tnh.baseware.core.annotations;

import com.tnh.baseware.core.services.MessageService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeWithFieldNameValidator implements ConstraintValidator<SizeWithFieldName, String> {

    final MessageService messageService;
    String fieldName;
    int min;
    int max;

    @Override
    public void initialize(SizeWithFieldName constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && value.length() >= min && value.length() <= max) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                messageService.getMessage(context.getDefaultConstraintMessageTemplate())
                        .replace("{0}", fieldName)
                        .replace("{1}", String.valueOf(min))
                        .replace("{2}", String.valueOf(max))
        ).addConstraintViolation();

        return false;
    }
}
