package com.best_store.right_bite.service.user.update;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

public interface UpdatableUserService {
    BaseUserInfo updateUser(@NonNull @Valid UserUpdateRequestDto userUpdateRequestDto, @NonNull Authentication authentication);

    BaseUserInfo findUserBy(@NonNull String email);

    BaseUserInfo findUserBy(@NonNull Long id);

    BaseUserInfo findUserBy(@NonNull Authentication authentication);

    void deleteUserBy(@NonNull Authentication authentication);

}
