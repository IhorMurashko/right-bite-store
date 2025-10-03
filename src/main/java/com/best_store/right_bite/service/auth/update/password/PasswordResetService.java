package com.best_store.right_bite.service.auth.update.password;

import com.best_store.right_bite.exception.exceptions.user.UserNotFoundException;
import jakarta.validation.constraints.NotNull;

/**
 * Functional interface for handling user password reset operations.
 */
@FunctionalInterface
public interface PasswordResetService {
    /**
     * Resets the user's password and sends the new password to their email.
     *
     * @param email the email address of the user whose password is to be reset; must not be null.
     * @throws UserNotFoundException if the specified email does not exist in the system.
     */
    void resetPassword(@NotNull String email);
}
