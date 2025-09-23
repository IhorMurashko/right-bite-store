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

}