package com.best_store.right_bite.security.constant;

import lombok.experimental.UtilityClass;

/**
 * Contains constant keys used to extract user details from Google OAuth2 user attributes.
 *
 * <p>
 * These constants are utilized to map Google's user profile attributes (e.g., email, first name)
 * to application-specific keys for processing and storage.
 * </p>
 */
@UtilityClass
public class GoogleCredentialsConstants {
    public String EMAIL = "email";
    public String FIRST_NAME = "given_name";
    public String LAST_NAME = "family_name";
    public String PICTURE = "picture";
    public String OAUTH_ID = "sub";
}