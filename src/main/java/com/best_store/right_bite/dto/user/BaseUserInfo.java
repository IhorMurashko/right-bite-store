package com.best_store.right_bite.dto.user;

import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;

import java.util.Set;

/**
 * Base interface representing minimal user identity information.
 *
 * <p>Defines essential user identification fields used across the system.</p>
 *
 * <p>Implemented by various DTOs to standardize access to user ID and email.</p>
 *
 * @author Ihor Murashko
 */
public interface BaseUserInfo {
    /**
     * Returns the unique identifier of the user.
     *
     * @return user ID
     */
    long id();

    /**
     * Returns the email address of the user.
     *
     * @return user email
     */
    String email();
    /**
     * Returns roles of the user.
     *
     * @return user roles
     */
    Set<String> roles();
}

