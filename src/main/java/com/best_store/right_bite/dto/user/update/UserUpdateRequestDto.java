package com.best_store.right_bite.dto.user.update;


import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.lang.Nullable;

import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.*;

/**
 * DTO for user profile update requests.
 * All fields are optional (except ID); only non-null values will be processed and validated.
 *
 * @author Ihor Murashko
 */
@Builder
public record UserUpdateRequestDto(
        @Nullable
        @Pattern(regexp = EMAIL_PATTERN,
                message = EMAIL_MESSAGE)
        String email,
        @Nullable
        @Pattern(regexp = PASSWORD_PATTERN,
                message = PASSWORD_MESSAGE)
        String password,
        @Nullable
        @Pattern(regexp = NAME_PATTERN,
                message = NAME_MESSAGE)
        String firstName,
        @Nullable
        @Pattern(regexp = NAME_PATTERN,
                message = NAME_MESSAGE)
        String lastName,
        @Nullable
        @Pattern(regexp = PHONE_PATTERN,
                message = PHONE_MESSAGE)
        String phoneNumber,
        @Nullable
        @Pattern(regexp = ADDRESS_PATTERN,
                message = ADDRESS_MESSAGE)
        String country,
        @Nullable
        @Pattern(regexp = ADDRESS_PATTERN,
                message = ADDRESS_MESSAGE)
        String city,
        @Nullable
        @Pattern(regexp = ADDRESS_PATTERN,
                message = ADDRESS_MESSAGE)
        String streetName,
        @Nullable
        @Pattern(regexp = HOUSE_NUMBER_PATTERN,
                message = HOUSE_NUMBER_MESSAGE)
        String houseNumber,

        @Nullable
        @Pattern(regexp = ZIP_CODE_PATTERN,
                message = ZIP_CODE_MESSAGE)
        String zipCode
) {
}

