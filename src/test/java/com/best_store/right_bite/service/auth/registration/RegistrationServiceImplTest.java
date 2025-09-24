package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.constant.constraint.user.UserFieldsConstraint;
import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.exception.exceptions.auth.CredentialsException;
import com.best_store.right_bite.exception.exceptions.role.RoleExceptionMP;
import com.best_store.right_bite.exception.exceptions.role.RoleNotFoundException;
import com.best_store.right_bite.exception.messageProvider.AuthExceptionMP;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.service.notificationService.dispatch.NotificationDispatcherService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.user.UserAssembler;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    @Mock
    private UserCrudService userCrudService;
    @Mock
    private UserAssembler userAssembler;
    @Mock
    private NotificationDispatcherService notificationDispatcherService;
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;
    private static Validator validator;
    private String email;
    private String password;
    private RegistrationCredentialsDto registrationCredentialsDto;
    private User user;
    private RoleName roleName;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        this.email = "email@email.com";
        this.password = "password25";
        this.registrationCredentialsDto = new RegistrationCredentialsDto(email, password, password);
        this.user = new User();
        this.roleName = RoleName.ROLE_USER;
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowConstraintViolationException_when_emailIsNullOrEmpty(String email) {
        RegistrationCredentialsDto authRequest = new RegistrationCredentialsDto(email, password, password);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(UserFieldsConstraint.EMAIL_MESSAGE, messages.iterator().next());
        verifyNoInteractions(userCrudService, userAssembler, notificationDispatcherService);
    }

    @ParameterizedTest
    @MethodSource("emailIsInvalidProvider")
    void shouldThrowConstraintViolationException_when_emailIsInvalid(String email) {
        RegistrationCredentialsDto authRequest = new RegistrationCredentialsDto(email, password, password);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(UserFieldsConstraint.EMAIL_MESSAGE, messages.iterator().next());
        verifyNoInteractions(userCrudService, userAssembler, notificationDispatcherService);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowConstraintViolationException_when_passwordIsNullOrEmpty(String password) {
        RegistrationCredentialsDto authRequest = new RegistrationCredentialsDto(email, password, password);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(UserFieldsConstraint.PASSWORD_MESSAGE, messages.iterator().next());
        verifyNoInteractions(userCrudService, userAssembler, notificationDispatcherService);
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordProvider")
    void shouldThrowConstraintViolationException_when_passwordIsInvalid(String password) {
        RegistrationCredentialsDto authRequest = new RegistrationCredentialsDto(email, password, password);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(UserFieldsConstraint.PASSWORD_MESSAGE, messages.iterator().next());
        verifyNoInteractions(userCrudService, userAssembler, notificationDispatcherService);
    }

    @Test
    void shouldThrowCredentialsException_when_emailAlreadyExists() {
        doReturn(true).when(userCrudService).isEmailExist(anyString());

        CredentialsException exception = assertThrows(CredentialsException.class,
                () -> registrationService.registration(registrationCredentialsDto));

        assertEquals(String.format(AuthExceptionMP.EMAIL_ALREADY_EXIST, email), exception.getMessage());
        verify(userCrudService).isEmailExist(stringArgumentCaptor.capture());
        assertNotEquals(email.toUpperCase(), stringArgumentCaptor.getValue());
        assertEquals(UserFieldAdapter.toLower(email), stringArgumentCaptor.getValue());
        verify(userCrudService, times(1)).isEmailExist(anyString());
        verifyNoMoreInteractions(userCrudService);
        verifyNoInteractions(userAssembler, notificationDispatcherService);
    }

    @Test
    void shouldThrowCredentialsException_when_passwordDontMatch() {
        RegistrationCredentialsDto authRequest = new RegistrationCredentialsDto(email, password, "");
        doReturn(false).when(userCrudService).isEmailExist(anyString());

        CredentialsException exception = assertThrows(CredentialsException.class,
                () -> registrationService.registration(authRequest));

        assertEquals(AuthExceptionMP.PASSWORDS_DONT_MATCH, exception.getMessage());
        verify(userCrudService).isEmailExist(stringArgumentCaptor.capture());
        assertNotEquals(email.toUpperCase(), stringArgumentCaptor.getValue());
        assertEquals(UserFieldAdapter.toLower(email), stringArgumentCaptor.getValue());
        verify(userCrudService, times(1)).isEmailExist(anyString());
        verifyNoMoreInteractions(userCrudService);
        verifyNoInteractions(userAssembler, notificationDispatcherService);
    }

    @Test
    void shouldThrowRoleNotFoundException_when_userAssemblerComponentThrowsException() {
        doReturn(false).when(userCrudService).isEmailExist(anyString());
        doThrow(new RoleNotFoundException(String.format(RoleExceptionMP.ROLE_NOT_FOUND, roleName)))
                .when(userAssembler).create(any(RegistrationCredentialsDto.class));

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
                () -> registrationService.registration(registrationCredentialsDto));

        assertEquals(String.format(RoleExceptionMP.ROLE_NOT_FOUND, roleName), exception.getMessage());
        verify(userCrudService).isEmailExist(stringArgumentCaptor.capture());
        assertNotEquals(email.toUpperCase(), stringArgumentCaptor.getValue());
        assertEquals(UserFieldAdapter.toLower(email), stringArgumentCaptor.getValue());
        verify(userCrudService, times(1)).isEmailExist(anyString());
        verify(userAssembler, times(1)).create(any(RegistrationCredentialsDto.class));
        verifyNoMoreInteractions(userCrudService, userAssembler);
        verifyNoInteractions(notificationDispatcherService);
    }

    @Test
    void shouldReturnUser_when_registrationIsSuccessful() {
        DefaultUserInfoResponseDto defaultUserInfoResponseDto = new DefaultUserInfoResponseDto(
                1L, email, true, true, true, true,
                LocalDateTime.now().minusDays(20), null, null, null, null,
                null, null, null, null, null, null
        );
        doReturn(false).when(userCrudService).isEmailExist(anyString());
        doReturn(user).when(userAssembler).create(any(RegistrationCredentialsDto.class));
        doReturn(defaultUserInfoResponseDto).when(userCrudService).save(any(User.class));
        doNothing().when(notificationDispatcherService).send(any());

        registrationService.registration(registrationCredentialsDto);

        verify(userCrudService).isEmailExist(stringArgumentCaptor.capture());
        assertNotEquals(email.toUpperCase(), stringArgumentCaptor.getValue());
        assertEquals(UserFieldAdapter.toLower(email), stringArgumentCaptor.getValue());
        verify(userCrudService, times(1)).isEmailExist(anyString());
        verify(userAssembler, times(1)).create(any(RegistrationCredentialsDto.class));
        verify(notificationDispatcherService, times(1)).send(any());
        verifyNoMoreInteractions(userCrudService, userAssembler, notificationDispatcherService);
    }


    private static Stream<Arguments> emailIsInvalidProvider() {
        return Stream.of(
                Arguments.of("plainaddress"),
                Arguments.of("@no-local-part.com"),
                Arguments.of("user@"),
                Arguments.of("user@.com"),
                Arguments.of("user@com"),
                Arguments.of("user@domain."),
                Arguments.of("user@domain.c"),
                Arguments.of("user@domain,com"),
                Arguments.of("user@domain..com"),
                Arguments.of("user@-domain.com"),
                Arguments.of("user@domain-.com"),
                Arguments.of("user@domain!.com"),
                Arguments.of("user name@domain.com"),
                Arguments.of("user@do main.com")
        );
    }

    private static Stream<Arguments> invalidPasswordProvider() {
        return Stream.of(
                Arguments.of("abc"),
                Arguments.of("123"),
                Arguments.of("abcd"),
                Arguments.of("1234"),
                Arguments.of("!!!!"),
                Arguments.of("a!"),
                Arguments.of("12!"),
                Arguments.of("abc "),
                Arguments.of("   "),
                Arguments.of("абвг1"),
                Arguments.of("æøå1"),
                Arguments.of("a😊1"),
                Arguments.of("test"),
                Arguments.of("0000"),
                Arguments.of("////")
        );
    }
}