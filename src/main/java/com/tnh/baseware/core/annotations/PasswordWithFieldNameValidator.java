package com.tnh.baseware.core.annotations;

import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.utils.ValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordWithFieldNameValidator implements ConstraintValidator<PasswordWithFieldName, String> {

    final MessageService messageService;
    String fieldName;

    @Override
    public void initialize(PasswordWithFieldName constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ValidationUtils.isValidPassword(value)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageService.getMessage(context.getDefaultConstraintMessageTemplate()).replace("{0}", fieldName)
        ).addConstraintViolation();

        return false;
    }
}
