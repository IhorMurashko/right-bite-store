package com.best_store.right_bite.service.auth.login;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.exception.exceptions.auth.CredentialsException;
import com.best_store.right_bite.exception.exceptions.auth.UserAccountIsNotAvailableException;
import com.best_store.right_bite.exception.exceptions.user.UserNotFoundException;
import com.best_store.right_bite.exception.messageProvider.AuthExceptionMP;
import com.best_store.right_bite.exception.messageProvider.UserExceptionMP;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.managment.TokenManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailPasswordAuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenManager tokenManager;
    @Mock
    private DefaultUserInfoDtoMapper defaultUserInfoDtoMapper;
    @InjectMocks
    private EmailPasswordAuthenticationServiceImpl authenticationService;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;
    private static Validator validator;
    private AuthRequest authRequest;
    private String email;
    private String password;
    private User user;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        this.email = "email@email.com";
        this.password = "password";
        this.authRequest = new AuthRequest(email, password);
        this.user = new User();
        this.user.setEmail(email);
        this.user.setPassword(password);
        this.tokenDto = new TokenDto("access", "refresh");
    }

    @Test
    void shouldThrowValidationConstraintViolationException_when_EmailIsNull() {
        AuthRequest authRequest = new AuthRequest(null, password);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(1, messages.size());
        assertTrue(messages.contains(AuthExceptionMP.EMAIL_FIELD_CANT_BE_EMPTY_OR_NULL));
        verifyNoInteractions(userRepository, passwordEncoder, tokenManager);
    }

    @Test
    void shouldThrowValidationConstraintViolationException_when_EmailIsEmpty() {
        AuthRequest authRequest = new AuthRequest("      ", password);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(1, messages.size());
        assertTrue(messages.contains(AuthExceptionMP.EMAIL_FIELD_CANT_BE_EMPTY_OR_NULL));
        verifyNoInteractions(userRepository, passwordEncoder, tokenManager);
    }

    @Test
    void shouldThrowValidationConstraintViolationException_when_PasswordIsNull() {
        AuthRequest authRequest = new AuthRequest(email, null);

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(1, messages.size());
        assertTrue(messages.contains(AuthExceptionMP.PASSWORD_FIELD_CANT_BE_EMPTY_OR_NULL));
        verifyNoInteractions(userRepository, passwordEncoder, tokenManager);
    }

    @Test
    void shouldThrowValidationConstraintViolationException_when_PasswordIsEmpty() {
        AuthRequest authRequest = new AuthRequest(email, "       ");

        Set<String> messages = validator.validate(authRequest).stream().map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(1, messages.size());
        assertTrue(messages.contains(AuthExceptionMP.PASSWORD_FIELD_CANT_BE_EMPTY_OR_NULL));
        verifyNoInteractions(userRepository, passwordEncoder, tokenManager);
    }

    @Test
    void shouldThrowUserNotFoundException_when_userEmailWasNotFound() {
        doReturn(Optional.empty())
                .when(userRepository).findByEmail(anyString());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, ()
                -> authenticationService.authenticate(authRequest));

        assertEquals(String.format(UserExceptionMP.USER_EMAIL_NOT_FOUND, email), exception.getMessage());
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, tokenManager);
    }

    @Test
    void shouldThrowCredentialsException_when_userPasswordWasInvalid() {
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());
        doReturn(false).when(passwordEncoder).matches(anyString(), anyString());

        CredentialsException exception = assertThrows(CredentialsException.class, ()
                -> authenticationService.authenticate(authRequest));

        assertEquals(AuthExceptionMP.PASSWORDS_DONT_MATCH, exception.getMessage());
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verify(passwordEncoder).matches(anyString(), anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(tokenManager);
    }

    @Test
    void shouldThrowUserAccountIsNotAvailableException_when_userAccountIsExpired() {
        user.setAccountNonExpired(false);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        UserAccountIsNotAvailableException exception = assertThrows(UserAccountIsNotAvailableException.class, ()
                -> authenticationService.authenticate(authRequest));

        assertNotNull(exception.getMessage());
        assertEquals(AuthExceptionMP.USER_ACCOUNT_IS_EXPIRED, exception.getMessage());
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verify(passwordEncoder).matches(anyString(), anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(tokenManager);
    }

    @Test
    void shouldThrowUserAccountIsNotAvailableException_when_userAccountIsLocked() {
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(false);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        UserAccountIsNotAvailableException exception = assertThrows(UserAccountIsNotAvailableException.class, ()
                -> authenticationService.authenticate(authRequest));

        assertNotNull(exception.getMessage());
        assertEquals(AuthExceptionMP.USER_ACCOUNT_IS_EXPIRED, exception.getMessage());
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verify(passwordEncoder).matches(anyString(), anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(tokenManager);
    }

    @Test
    void shouldThrowUserAccountIsNotAvailableException_when_userCredentialsIsExpired() {
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(false);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        UserAccountIsNotAvailableException exception = assertThrows(UserAccountIsNotAvailableException.class, ()
                -> authenticationService.authenticate(authRequest));

        assertNotNull(exception.getMessage());
        assertEquals(AuthExceptionMP.USER_ACCOUNT_IS_EXPIRED, exception.getMessage());
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verify(passwordEncoder).matches(anyString(), anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(tokenManager);
    }

    @Test
    void shouldThrowUserAccountIsNotAvailableException_when_userIsNotEnabled() {
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());

        UserAccountIsNotAvailableException exception = assertThrows(UserAccountIsNotAvailableException.class, ()
                -> authenticationService.authenticate(authRequest));

        assertNotNull(exception.getMessage());
        assertEquals(AuthExceptionMP.USER_ACCOUNT_IS_EXPIRED, exception.getMessage());
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verify(passwordEncoder).matches(anyString(), anyString());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(tokenManager);
    }

    @Test
    void shouldReturnTokensDto_when_userIsAvailable() {
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        DefaultUserInfoResponseDto userInfoResponseDto = new DefaultUserInfoResponseDto(
                1L, email, true, true, true, true,
                LocalDateTime.now().minusDays(20), "name", "lastname", null, "123456789",
                "country", "city", "street", "12", "8561", Set.of(RoleName.ROLE_USER.name())
        );
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());
        doReturn(true).when(passwordEncoder).matches(anyString(), anyString());
        doReturn(userInfoResponseDto).when(defaultUserInfoDtoMapper).toDTO(any(User.class));
        doReturn(tokenDto).when(tokenManager).generateDefaultTokens(any());

        TokenDto dto = authenticationService.authenticate(authRequest);

        assertNotNull(dto);
        assertEquals(tokenDto, dto);
        verify(userRepository).findByEmail(stringArgumentCaptor.capture());
        assertEquals(email, stringArgumentCaptor.getValue());
        verify(passwordEncoder).matches(anyString(), anyString());
        verify(tokenManager).generateDefaultTokens(userInfoResponseDto);
        verifyNoMoreInteractions(userRepository, passwordEncoder, tokenManager);
    }
}