package com.best_store.right_bite.service.user.update;

import com.best_store.right_bite.dto.user.BaseUserInfo;
import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.exception.messageProvider.SecurityExceptionMP;
import com.best_store.right_bite.exception.messageProvider.UserExceptionMP;
import com.best_store.right_bite.exception.exceptions.user.UserNotFoundException;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.mapper.user.UpdatableUserInfoMapper;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.security.exception.InvalidTokenSubjectException;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
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

    private final UserRepository userRepository;
    private final UpdatableUserInfoMapper updatableUserInfoMapper;
    private final DefaultUserInfoDtoMapper defaultUserInfoDtoMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseUserInfo updateUser(@NonNull @Valid UserUpdateRequestDto userUpdateRequestDto,
                                   @NonNull JwtPrincipal principal) {

        try {
            Long id = Long.parseLong(principal.id());
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format(UserExceptionMP.USER_ID_NOT_FOUND, id))
                    );
            log.debug("user with id was {} was found", id);
            updatableUserInfoMapper.updateEntityFromDto(userUpdateRequestDto, user);
            User saved = userRepository.save(user);
            log.info("user with id {} was updated", id);
            return defaultUserInfoDtoMapper.toDTO(saved);
        } catch (NumberFormatException ex) {
            throw new InvalidTokenSubjectException(
                    String.format(
                            SecurityExceptionMP.INVALID_TOKEN_SUBJECT,
                            ex.getClass().getSimpleName()));
        }
    }
}
