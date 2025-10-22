package com.best_store.right_bite.security.claims;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.function.Function;

/**
 * Functional interface for extracting specific claims from a JWT token.
 *
 * <p>
 * This abstraction allows decoupling token parsing logic from the actual claim extraction logic,
 * supporting flexible usage via functional mapping.
 * </p>
 *
 * @author Ihor Murashko
 */
@FunctionalInterface
public interface ClaimsProvider {
    /**
     * Extracts a claim from the provided JWT token using a functional mapper.
     *
     * @param token    the raw JWT token string
     * @param function a function that maps the {@link Claims} object to desired value
     * @param <T>      the type of the value to be extracted
     * @return the extracted claim value
     */
    <T> T extractClaimFromToken(String token, SecretKey key, Function<Claims, T> function);
}
