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
public class UsernameWithFieldNameValidator implements ConstraintValidator<UsernameWithFieldName, String> {

    final MessageService messageService;
    String fieldName;

    @Override
    public void initialize(UsernameWithFieldName constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ValidationUtils.isValidUsername(value)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageService.getMessage(context.getDefaultConstraintMessageTemplate().replace("{0}", fieldName))
        ).addConstraintViolation();

        return false;
    }
}
