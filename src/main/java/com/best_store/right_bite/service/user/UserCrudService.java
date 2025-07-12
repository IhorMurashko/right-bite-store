package com.best_store.right_bite.service.user;


import com.best_store.right_bite.model.user.User;
import org.springframework.lang.NonNull;

public interface UserCrudService {

    User findByEmail(@NonNull String email);

    User findById(@NonNull Long id);

    User save(@NonNull User user);

    void deleteById(@NonNull long id);

    void deleteByEmail(@NonNull String email);

    boolean isEmailExist(@NonNull String email);

    boolean isUserExistById(@NonNull long id);

    void resetPasswordByEmail(@NonNull String email, @NonNull String newEncodedPassword);
}