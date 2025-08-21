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
import com.best_store.right_bite.utils.security.AuthenticationParserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Implementation of {@link UpdatableUserService} for managing updates and retrieval
 * of authenticated and other users.
 * <p>
 * Resolves user identity from JWT, applies updates via mappers,
 * and persists changes using {@link UserCrudService}.
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UpdatableUserServiceImpl implements UpdatableUserService {

    private final UserCrudService userCrudService;
    private final UpdatableUserInfoMapper updatableUserInfoMapper;
    private final DefaultUserInfoDtoMapper defaultUserInfoDtoMapper;
    private final AuthenticationParserUtil authenticationParserUtil;

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseUserInfo updateUser(@NonNull @Valid UserUpdateRequestDto userUpdateRequestDto,
                                   @NonNull Authentication authentication) {

        Long id = authenticationParserUtil.getUserLongIdFromAuthentication(authentication);

        try {
            User user = userCrudService.findById(id);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseUserInfo findUserBy(@NonNull String email) {
        User user = userCrudService.findByEmail(email);
        return defaultUserInfoDtoMapper.toDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseUserInfo findUserBy(@NonNull Long id) {
        User user = userCrudService.findById(id);
        return defaultUserInfoDtoMapper.toDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseUserInfo findUserBy(@NonNull Authentication authentication) {
        User user = userCrudService.findById(authenticationParserUtil.getUserLongIdFromAuthentication(authentication));
        return defaultUserInfoDtoMapper.toDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserBy(@NonNull Authentication authentication) {
        userCrudService.deleteById(authenticationParserUtil.getUserLongIdFromAuthentication(authentication));
    }
}
