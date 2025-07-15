package com.best_store.right_bite.dto.auth.registration;

import io.swagger.v3.oas.annotations.media.Schema;
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

//TODO пытаться разрешить конфликты, что бы всё заработало.
@Schema(name = "credentials for registration using email and password")
public record RegistrationCredentialsDto(
        @Schema(description = "user's email",
        pattern =  EMAIL_PATTERN,
        nullable = false)
        @NonNull
        @Email(regexp = EMAIL_PATTERN,
                message = EMAIL_MESSAGE)
        String email,

        @Schema(description = "user's password",
        pattern = PASSWORD_PATTERN,
        nullable = false)
        @NonNull
        @Pattern(regexp = PASSWORD_PATTERN,
                message = PASSWORD_MESSAGE)
        String password,

        @Schema(description = "user's confirmation password",
                nullable = false)
        @NonNull
        String confirmationPassword
) implements Serializable {
}