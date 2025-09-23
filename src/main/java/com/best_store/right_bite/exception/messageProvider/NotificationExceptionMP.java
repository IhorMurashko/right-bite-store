package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationExceptionMP {
    public final String SENDER_BY_CHANNEL_WAS_NOT_FOUND = "Sender with channel type: %s was not found";
    public final String WRONG_NOTIFICATION_DATA_TYPE = "Wrong notification data type %s";
    public final String EMPTY_NOTIFICATION_DATA = "Notification data cant be empty";
    public final String NOTIFICATION_BUILDER_AND_TYPES_ARE_NOT_INCOMPATIBLE = "Builder and notification types are incompatible %s";
    public final String FAILED_TO_PROCESS_NOTIFICATION_CONTENT = "Failed to process notification content %s";
    public final String NOTIFICATION_BUILDER_WAS_NOT_FOUND = "Notification builder with type: %s and channel: %s was not found.";

}
