package com.best_store.right_bite.security.principal;

/**
 * Represents a principal for JWT-based authentication.
 * Encapsulates the user's unique identifier (ID) and email address extracted
 * from the JWT token claims.
 *
 * @param id    the unique identifier of the user.
 * @param email the email address of the user.
 */
public record JwtPrincipal(String id, String email) {
}
