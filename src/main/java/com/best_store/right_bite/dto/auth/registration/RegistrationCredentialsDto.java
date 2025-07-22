package com.best_store.right_bite.dto.auth.registration;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.lang.NonNull;

import java.io.Serializable;

import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.*;
/**
 * DTO representing user credentials for registration via email and password.
 *
 * <p>Used in the registration flow to capture and validate user input.</p>
 *
 * <ul>
 *     <li><b>email</b> — required, must match email pattern</li>
 *     <li><b>password</b> — required, must match password constraints</li>
 *     <li><b>confirmationPassword</b> — required, must match password on service level</li>
 * </ul>
 *
 * <p>Validation annotations ensure basic format compliance; logical consistency
 * (e.g. password match) must be handled in service layer.</p>
 *
 * @author Ihor Murashko
 */
@Schema(name = "Credentials for registration using email and password")
public record RegistrationCredentialsDto(
        @Schema(description = "User's email", pattern = EMAIL_PATTERN, nullable = false)
        @NonNull
        @Email(regexp = EMAIL_PATTERN, message = EMAIL_MESSAGE)
        String email,

        @Schema(description = "User's password", pattern = PASSWORD_PATTERN, nullable = false)
        @NonNull
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        String password,

        @Schema(description = "Password confirmation (must match password)", nullable = false)
        @NonNull
        String confirmationPassword
) implements Serializable {
}