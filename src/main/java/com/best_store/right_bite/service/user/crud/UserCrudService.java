package com.best_store.right_bite.service.user.crud;


import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.user.User;
import org.springframework.lang.NonNull;

public interface UserCrudService {

    DefaultUserInfoResponseDto findByEmail(@NonNull String email);

    DefaultUserInfoResponseDto findById(@NonNull Long id);

    /**
     * Saves or updates the given user.
     *
     * @param user user entity to persist
     * @return the saved {@link DefaultUserInfoResponseDto}
     */
    DefaultUserInfoResponseDto save(@NonNull User user);

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
     * Resets the password for a user with a given email.
     *
     * @param email              user email
     * @param newEncodedPassword new encoded password to store
     */
    void resetPasswordByEmail(@NonNull String email, @NonNull String newEncodedPassword);
}
