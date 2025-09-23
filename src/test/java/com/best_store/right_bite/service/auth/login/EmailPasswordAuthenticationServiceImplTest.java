package com.best_store.right_bite.service.auth.login;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.exception.exceptions.auth.CredentialsException;
import com.best_store.right_bite.exception.exceptions.auth.UserAccountIsNotAvailableException;
import com.best_store.right_bite.exception.exceptions.user.UserNotFoundException;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.managment.TokenManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void authenticate_successful() {
        String email = "test@domain.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        AuthRequest authRequest = new AuthRequest(email, password);
        User user = mock(User.class);
        DefaultUserInfoResponseDto userDto = mock(DefaultUserInfoResponseDto.class);
        TokenDto tokenDto = new TokenDto("accessToken", "refreshToken");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(user.isAccountNonExpired()).thenReturn(true);
        when(user.isAccountNonLocked()).thenReturn(true);
        when(user.isCredentialsNonExpired()).thenReturn(true);
        when(user.isEnabled()).thenReturn(true);
        when(defaultUserInfoDtoMapper.toDTO(user)).thenReturn(userDto);
        when(tokenManager.generateDefaultTokens(userDto)).thenReturn(tokenDto);

        TokenDto result = authenticationService.authenticate(authRequest);

        assertNotNull(result);
        assertEquals(tokenDto.accessToken(), result.accessToken());
        assertEquals(tokenDto.refreshToken(), result.refreshToken());
    }

//    @Test
//    void authenticate_userNotFound() {
//        String email = "test@domain.com";
//        String password = "password123";
//        AuthRequest authRequest = new AuthRequest(email, password);
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        UserNotFoundException exception =
//                assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(authRequest));
//
//        assertEquals("User email " + email + " not found!", exception.getMessage());
//    }
//
//    @Test
//    void authenticate_invalidPassword() {
//        String email = "test@domain.com";
//        String password = "password123";
//        String incorrectPassword = "wrongPassword";
//        AuthRequest authRequest = new AuthRequest(email, password);
//        User user = mock(User.class);
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);
//
//        CredentialsException exception =
//                assertThrows(CredentialsException.class, () -> authenticationService.authenticate(authRequest));
//
//        assertEquals("Passwords do not match!", exception.getMessage());
//    }
//
//    @Test
//    void authenticate_userAccountNotAvailable() {
//        String email = "test@domain.com";
//        String password = "password123";
//        AuthRequest authRequest = new AuthRequest(email, password);
//        User user = mock(User.class);
//
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
//        when(user.isAccountNonExpired()).thenReturn(false);
//
//        UserAccountIsNotAvailableException exception =
//                assertThrows(UserAccountIsNotAvailableException.class, () -> authenticationService.authenticate(authRequest));
//
//        assertEquals("User account is expired or disabled!", exception.getMessage());
//    }
}