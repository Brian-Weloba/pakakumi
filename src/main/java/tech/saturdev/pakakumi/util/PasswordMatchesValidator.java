package tech.saturdev.pakakumi.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tech.saturdev.pakakumi.security.login.models.User;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        User user = (User) obj;
        return passwordEncoder.matches(user.getMatchingPassword(), user.getPassword());
    }
}