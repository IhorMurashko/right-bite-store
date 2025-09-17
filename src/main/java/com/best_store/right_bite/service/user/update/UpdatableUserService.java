package com.best_store.right_bite.service.user.update;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.security.principal.JwtPrincipal;
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
     * @param principal            current authenticated user context
     * @return updated user info
     */
    BaseUserInfo updateUser(@NonNull @Valid UserUpdateRequestDto userUpdateRequestDto,
                            @NonNull JwtPrincipal principal);

}
