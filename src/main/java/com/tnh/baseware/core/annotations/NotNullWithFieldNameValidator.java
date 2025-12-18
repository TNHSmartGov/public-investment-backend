package com.tnh.baseware.core.annotations;

import com.tnh.baseware.core.services.MessageService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotNullWithFieldNameValidator implements ConstraintValidator<NotNullWithFieldName, Object> {

    final MessageService messageService;
    String fieldName;

    @Override
    public void initialize(NotNullWithFieldName constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value != null) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                messageService.getMessage(context.getDefaultConstraintMessageTemplate()).replace("{0}", fieldName)
        ).addConstraintViolation();

        return false;
    }
}
