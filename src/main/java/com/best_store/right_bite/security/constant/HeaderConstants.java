package com.best_store.right_bite.security.constant;

import lombok.experimental.UtilityClass;

/**
 * Defines constants for HTTP header keys used in security-related operations.
 *
 * <p>
 * Specifically, this class contains fields for the Authorization header
 * and the Bearer token prefix to ensure consistent usage throughout the application.
 * </p>
 */
@UtilityClass
public class HeaderConstants {
    public final String HEADER_AUTHENTICATION = "Authorization";
    public final String BEARER_PREFIX = "Bearer";
}