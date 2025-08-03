package com.best_store.right_bite.service.user.update;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.role.InvalidPrincipalCastException;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.mapper.user.UpdatableUserInfoMapper;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.security.exception.InvalidTokenSubjectException;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UpdatableUserServiceImpl implements UpdatableUserService {

    private final UserCrudService userCrudService;
    private final UpdatableUserInfoMapper updatableUserInfoMapper;
    private final DefaultUserInfoDtoMapper defaultUserInfoDtoMapper;

    @Override
    public BaseUserInfo updateUser(@NonNull @Valid UserUpdateRequestDto userUpdateRequestDto,
                                   @NonNull Authentication authentication) {

        if (!(authentication.getPrincipal() instanceof JwtPrincipal principal)) {
            log.error("Invalid principal type");
            throw new InvalidPrincipalCastException(
                    ExceptionMessageProvider.AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION);
        }
        String id = principal.id();

        try {
            User user = userCrudService.findById(Long.parseLong(id));
            log.debug("user with id was {} was found", id);
            updatableUserInfoMapper.updateEntityFromDto(userUpdateRequestDto, user);
            User saved = userCrudService.save(user);
            log.info("user with id {} was updated", id);
            return defaultUserInfoDtoMapper.toDTO(saved);
        } catch (NumberFormatException ex) {
            throw new InvalidTokenSubjectException(
                    String.format(
                            ExceptionMessageProvider.INVALID_TOKEN_SUBJECT,
                            ex.getClass().getSimpleName()));
        }

    }

    @Override
    public BaseUserInfo findUserBy(@NonNull String email) {
        User user = userCrudService.findByEmail(email);
        return defaultUserInfoDtoMapper.toDTO(user);
    }

    @Override
    public BaseUserInfo findUserBy(@NonNull Long id) {
        User user = userCrudService.findById(id);
        return defaultUserInfoDtoMapper.toDTO(user);
    }

    @Override
    public BaseUserInfo findUserBy(@NonNull Authentication authentication) {
        if (authentication.getPrincipal() instanceof JwtPrincipal principal) {
            User user = userCrudService.findById(Long.parseLong(principal.id()));
            return defaultUserInfoDtoMapper.toDTO(user);
        } else {
            throw new InvalidPrincipalCastException(
                    ExceptionMessageProvider.AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION);
        }
    }

    @Override
    public void deleteUserBy(@NonNull Authentication authentication) {
        if (authentication.getPrincipal() instanceof JwtPrincipal principal) {
            userCrudService.deleteById(Long.parseLong(principal.id()));
        } else {
            throw new InvalidPrincipalCastException(
                    ExceptionMessageProvider.AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION);
        }
    }
}
