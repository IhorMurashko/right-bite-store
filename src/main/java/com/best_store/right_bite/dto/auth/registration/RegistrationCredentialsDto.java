package com.best_store.right_bite.dto.auth.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.lang.NonNull;

import java.io.Serializable;

import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.*;

/**
 * DTO used for user registration.
 * Contains required fields to create a new user account.
 *
 * @author Ihor Murashko
 */
public record RegistrationCredentialsDto(
        @NonNull
        @Email(regexp = EMAIL_PATTERN,
                message = EMAIL_MESSAGE)
        String email,

        @NonNull
        @Pattern(regexp = PASSWORD_PATTERN,
                message = PASSWORD_MESSAGE)
        String password,

        @NonNull
        String confirmationPassword
) implements Serializable { }