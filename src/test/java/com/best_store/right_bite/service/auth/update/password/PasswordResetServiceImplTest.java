package com.best_store.right_bite.service.auth.update.password;

import com.best_store.right_bite.exception.exceptions.user.UserNotFoundException;
import com.best_store.right_bite.exception.messageProvider.UserExceptionMP;
import com.best_store.right_bite.service.notificationService.dispatch.NotificationDispatcherService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserCrudService userCrudService;
    @Mock
    private NotificationDispatcherService notificationDispatcherService;
    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;
    private String email;

    @BeforeEach
    void setUp() {
        this.email = "email@email.com";
    }

    @Test
    void shouldThrowCUserNotFoundException_when_emailWasNotFound() {
        doReturn(false).when(userCrudService).isEmailExist(anyString());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                passwordResetService.resetPassword(email));

        assertEquals(String.format(UserExceptionMP.USER_EMAIL_NOT_FOUND, email), exception.getMessage());
        verify(userCrudService, times(1)).isEmailExist(anyString());
        verify(userCrudService).isEmailExist(stringArgumentCaptor.capture());
        assertNotEquals(email.toUpperCase(), stringArgumentCaptor.getValue());
        assertEquals(email, stringArgumentCaptor.getValue());
        verifyNoMoreInteractions(userCrudService);
        verifyNoInteractions(passwordEncoder, notificationDispatcherService);
    }

    @Test
    void shouldThrowRuntimeException_when_userCurdServiceThrowsException() {
        doReturn(true).when(userCrudService).isEmailExist(anyString());
        doReturn(UUID.randomUUID().toString()).when(passwordEncoder).encode(anyString());
        doThrow(RuntimeException.class).when(userCrudService).resetPasswordByEmail(anyString(), anyString());

        assertThrows(RuntimeException.class, () -> passwordResetService.resetPassword(email));

        verify(userCrudService, times(1)).isEmailExist(anyString());
        verify(userCrudService, times(1)).resetPasswordByEmail(anyString(), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(passwordEncoder).encode(stringArgumentCaptor.capture());
        assertNotNull(stringArgumentCaptor.getValue());
        assertEquals(6, stringArgumentCaptor.getValue().length());
        verifyNoMoreInteractions(userCrudService, passwordEncoder);
        verifyNoInteractions(notificationDispatcherService);
    }

    @Test
    void shouldSuccessfullyResetPassword() {
        doReturn(true).when(userCrudService).isEmailExist(anyString());
        doReturn(UUID.randomUUID().toString()).when(passwordEncoder).encode(anyString());
        doNothing().when(userCrudService).resetPasswordByEmail(anyString(), anyString());
        doNothing().when(notificationDispatcherService).send(any());

        passwordResetService.resetPassword(email);

        verify(userCrudService, times(1)).isEmailExist(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(passwordEncoder).encode(stringArgumentCaptor.capture());
        assertNotNull(stringArgumentCaptor.getValue());
        assertEquals(6, stringArgumentCaptor.getValue().length());
        verify(userCrudService, times(1)).resetPasswordByEmail(anyString(), anyString());
        verify(notificationDispatcherService, times(1)).send(any());
        verifyNoMoreInteractions(userCrudService, passwordEncoder, notificationDispatcherService);
    }
}