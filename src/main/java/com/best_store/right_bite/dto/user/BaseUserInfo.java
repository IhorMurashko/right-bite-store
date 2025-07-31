package com.best_store.right_bite.dto.user;

import java.util.Set;

public interface BaseUserInfo {
    long id();

    String email();

    Set<String> roles();
}
