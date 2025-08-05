package com.best_store.right_bite.service.user.update;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

/**
 * Service for managing user updates and retrievals.
 * <p>
 * Provides operations for updating personal user information,
 * retrieving user data based on different identifiers, and deleting users.
 * <p>
 * Requires authenticated user context for some methods.
 */
public interface UpdatableUserService {
    /**
     * Updates the current user's profile with the provided data.
     * Only non-null fields will be updated.
     *
     * @param userUpdateRequestDto DTO with updated fields
     * @param authentication       current authenticated user context
     * @return updated user info
     */
    BaseUserInfo updateUser(@NonNull @Valid UserUpdateRequestDto userUpdateRequestDto,
                            @NonNull Authentication authentication);

    /**
     * Finds a user by email.
     *
     * @param email user email
     * @return user info
     */
    BaseUserInfo findUserBy(@NonNull String email);

    /**
     * Finds a user by ID.
     *
     * @param id user ID
     * @return user info
     */
    BaseUserInfo findUserBy(@NonNull Long id);

    /**
     * Finds the current user using authentication token.
     *
     * @param authentication current user context
     * @return user info
     */
    BaseUserInfo findUserBy(@NonNull Authentication authentication);

    /**
     * Deletes the currently authenticated user.
     *
     * @param authentication current user context
     */
    void deleteUserBy(@NonNull Authentication authentication);
}
