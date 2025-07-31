package com.best_store.right_bite.service.user.crud;


import com.best_store.right_bite.model.user.User;
import org.springframework.lang.NonNull;
/**
 * Interface defining basic CRUD and utility operations for {@link User} entities.
 *
 * <p>Provides methods to retrieve, create, delete users and check their existence
 * by email or ID. Also allows password reset by email.</p>
 *
 * <p>All operations are email- or ID-based to ensure consistent access patterns.</p>
 *
 * @author Ihor Murashko
 */
public interface UserCrudService {

    /**
     * Retrieves a user by email.
     *
     * @param email user's email
     * @return the user entity
     */
    User findByEmail(@NonNull String email);

    /**
     * Retrieves a user by ID.
     *
     * @param id user ID
     * @return the user entity
     */
    User findById(@NonNull Long id);

    /**
     * Saves a new or updated user.
     *
     * @param user user entity to save
     * @return saved user
     */
    User save(@NonNull User user);

    /**
     * Deletes a user by ID.
     *
     * @param id user ID
     */
    void deleteById(@NonNull long id);

    /**
     * Deletes a user by email.
     *
     * @param email user email
     */
    void deleteByEmail(@NonNull String email);

    /**
     * Checks whether a user with given email exists.
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
    boolean isUserExistById(@NonNull long id);

    /**
     * Resets the password for a user with given email.
     *
     * @param email              user email
     * @param newEncodedPassword new encoded password to store
     */
    void resetPasswordByEmail(@NonNull String email, @NonNull String newEncodedPassword);
}
