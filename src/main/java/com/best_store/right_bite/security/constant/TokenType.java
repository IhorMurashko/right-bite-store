package com.best_store.right_bite.security.constant;

/**
 * Enumerates supported types of security tokens in the system.
 *
 * <ul>
 *     <li>{@code ACCESS} — used for authenticated access to resources.</li>
 *     <li>{@code REFRESH} — used to renew access tokens without re-authentication.</li>
 * </ul>
 */
public enum TokenType {
    REFRESH, ACCESS
}
