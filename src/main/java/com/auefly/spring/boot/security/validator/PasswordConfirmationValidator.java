package com.auefly.spring.boot.security.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, Object> {
    private String password;
    private String confirmPassword;
    private String message;
    @Override
    public void initialize(PasswordConfirmation constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(o).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(o).getPropertyValue(confirmPassword);
        boolean isValid = passwordValue != null && passwordValue.equals(confirmPasswordValue);
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword").addConstraintViolation();
        }

        return isValid;
    }
}
