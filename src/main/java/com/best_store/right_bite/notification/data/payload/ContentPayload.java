package com.best_store.right_bite.notification.data.payload;

public sealed interface ContentPayload permits
        SimpleStringContentPayload, ResetPasswordContentPayload {
}
