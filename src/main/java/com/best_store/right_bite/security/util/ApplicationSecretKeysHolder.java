package com.best_store.right_bite.security.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class ApplicationSecretKeysHolder {

    private final SecretKey jwtAccessSecretKey;
    private final SecretKey jwtRefreshSecretKey;
}
