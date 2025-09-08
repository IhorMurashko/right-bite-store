package com.best_store.right_bite.service.user.crud;


import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.user.User;
import org.springframework.lang.NonNull;

/**
 * Interface defining basic CRUD and utility operations for {@link User} entities.
 *
 * <p>Supports retrieval, creation, deletion of users and utility checks by email or ID.
 * Also provides password reset functionality.</p>
 *
 * <p>Designed for consistent and reusable user data access.</p>
 *
 * @author Ihor Murashko
 */
public interface UserCrudService {

    /**
     * Retrieves a user by email.
     *
     * @param email user's email
     * @return the found {@link User}
     * @throws UserNotFoundException if user is not found
     */
    User findByEmail(@NonNull String email);

    /**
     * Retrieves a user by ID.
     *
     * @param id user ID
     * @return the found {@link User}
     * @throws UserNotFoundException if user is not found
     */
    User findById(@NonNull Long id);

    /**
     * Saves or updates the given user.
     *
     * @param user user entity to persist
     * @return the saved {@link User}
     */
    User save(@NonNull User user);

    /**
     * Deletes a user by ID.
     *
     * @param id user ID
     */
    void deleteById(@NonNull Long id);

    /**
     * Deletes a user by email.
     *
     * @param email user email
     */
    void deleteByEmail(@NonNull String email);

    /**
     * Checks if a user exists by email.
     *
     * @param email user email
     * @return true if exists, false otherwise
     */
    boolean isEmailExist(@NonNull String email);

    /**
     * Checks whether a user with given ID exists.
     *
     * @param id user ID
     * @return true if exists, false otherwise
     */
    boolean isUserExistById(@NonNull Long id);

    /**
     * Resets the password for a user with given email.
     *
     * @param email              user email
     * @param newEncodedPassword new encoded password to store
     */
    void resetPasswordByEmail(@NonNull String email, @NonNull String newEncodedPassword);
}
