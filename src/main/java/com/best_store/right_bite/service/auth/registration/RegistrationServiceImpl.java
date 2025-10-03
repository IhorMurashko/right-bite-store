package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.constant.notification.holder.letter.GreetingVariablesHolder;
import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.exceptions.auth.CredentialsException;
import com.best_store.right_bite.exception.messageProvider.AuthExceptionMP;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.notification.data.core.DefaultNotification;
import com.best_store.right_bite.notification.data.payload.SimpleStringContentPayload;
import com.best_store.right_bite.service.notificationService.dispatch.NotificationDispatcherService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.user.UserAssembler;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of the {@link RegistrationService} responsible for handling user registration logic.
 *
 * <p>This service performs the following operations:</p>
 * <ul>
 *   <li>Validates registration credentials</li>
 *   <li>Checks for duplicate email</li>
 *   <li>Verifies password confirmation</li>
 *   <li>Creates and persists a new {@link User} entity</li>
 *   <li>Sends a registration confirmation notification</li>
 * </ul>
 *
 * <p>Delegates user creation to {@link UserAssembler} and persistence to {@link UserCrudService}.</p>
 *
 * <p>Notifies the user via {@link NotificationDispatcherService} using an email channel upon success.</p>
 *
 * <p>Method is validated with {@code javax.validation.constraints} and {@code @Validated} annotation.</p>
 *
 * @author Ihor Murashko
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserCrudService userCrudService;
    private final UserAssembler userAssembler;
    private final NotificationDispatcherService notificationDispatcherService;

    /**
     * Registers a new user with the provided credentials.
     *
     * <p>Performs the following checks:</p>
     * <ul>
     *   <li>Lowercases the email for normalization</li>
     *   <li>Checks if the email already exists in the database</li>
     *   <li>Ensures password and confirmation password match</li>
     * </ul>
     *
     * <p>On success:</p>
     * <ul>
     *   <li>Creates and persists a new {@link User}</li>
     *   <li>Dispatches a confirmation email</li>
     * </ul>
     *
     * @param credentials DTO containing email, password, and confirmation password
     * @throws CredentialsException if the email already exists or passwords do not match
     */
    @Override
    public void registration(@NotNull @Valid RegistrationCredentialsDto credentials) {
        String email = UserFieldAdapter.toLower(credentials.email());
        if (userCrudService.isEmailExist(email)) {
            log.warn("Attempt to register already existing email: {}", email);
            throw new CredentialsException(
                    String.format(AuthExceptionMP.EMAIL_ALREADY_EXIST, email));
        }
        if (!Objects.equals(credentials.password(), credentials.confirmationPassword())) {
            log.error("Passwords do not match");
            throw new CredentialsException(AuthExceptionMP.PASSWORDS_DONT_MATCH);
        }
        User user = userAssembler.create(credentials);
        userCrudService.save(user);
        log.info("User {} saved successfully", email);
        SimpleStringContentPayload greeting = new SimpleStringContentPayload(GreetingVariablesHolder.GREETING);
        DefaultNotification greetingNotification = new DefaultNotification(
                NotificationType.SIMPLE_STRING_NOTIFICATION,
                NotificationChannel.EMAIL,
                List.of(email),
                GreetingVariablesHolder.EMAIL_SUBJECT,
                GreetingVariablesHolder.BODY_TITLE,
                greeting
        );
        notificationDispatcherService.send(greetingNotification);
    }
}