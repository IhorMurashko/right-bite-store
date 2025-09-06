package com.best_store.right_bite.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageProvider {
    public final String USER_EMAIL_NOT_FOUND = "User with email: %s not found";
    public final String USER_ID_NOT_FOUND = "User with ID: %d not found";
    public final String PASSWORDS_DONT_MATCH = "Passwords don't match";
    public final String EMAIL_ALREADY_EXIST = "Email: %s already exist";
    public final String INVALID_CREDENTIALS = "Invalid email or password.";
    public final String USER_ACCOUNT_IS_EXPIRED = "User account is expired.";
    public final String NOTIFICATION_CHANNEL_WAS_NOT_FOUND = "Notification type %s was not found";
    public final String NOTIFICATION_BUILDER_WAS_NOT_FOUND = "Notification builder with type: %s and channel: %s was not found.";
    public final String TOKEN_ACCESS_EXCEPTION = "Token with type %s can't be used for access for this endpoint.";
    public final String INVALID_TOKEN = "Invalid token.";
    public final String AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION = "Authentication principal is not instance of JwtPrincipal.";
    public final String INVALID_TOKEN_SUBJECT = "Invalid token subject. Subject couldn't be %s type.";
    public final String ID_PRODUCT_NOT_FOUND = "Product with id %s not found";
    public final String USER_CART_WAS_NOT_FOUND = "User cart with user id: %d not found";
    public final String INTERNAL_AI_CALL_ERROR = "Internal AI call error: %s";
    public final String NEWSLETTER_ID_WAS_NOT_FOUND = "Newsletter subscription with id %d not found";
    public final String NEWSLETTER_EMAIL_WAS_NOT_FOUND = "Newsletter subscription with email %s not found";
    public final String ORDER_WRONG_INCOMING_TYPE = "Wrong incoming type of order received: %s";
    public final String NOT_ENOUGH_QUANTITY_IN_STOCK = "Product with id: %d has not enough quantity in stock";
    public final String EMPTY_REQUIRED_DELIVERY_DETAIL = "Required delivery detail is empty";
    public final String FOUND_NULL_OBJECT = "Value can't be null";
    public final String USER_AUTHENTICATION_EXCEPTION = "User isn't authenticated";
    public final String WRONG_INCOMING_OBJECT_TYPE_EXCEPTION = "Wrong incoming object type: %s";
    public final String ORDER_ID_NOT_FOUND = "Order with ID: %d not found";
    public final String EMPTY_USER_CART = "Cannot create order from empty cart";
    public final String EMPTY_ORDER = "Order can't be empty";
    public final String ORDER_ACCESS_DENIED = "Access denied to order: %d";
}
