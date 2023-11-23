package com.auefly.spring.boot.security.validator;


import com.auefly.spring.boot.security.repository.CollectionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomUniqueValidator implements ConstraintValidator<CustomUnique, String> {
    @Autowired
    CollectionRepository collectionRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return collectionRepository.findFirstBySlug(value) == null;
    }
}
