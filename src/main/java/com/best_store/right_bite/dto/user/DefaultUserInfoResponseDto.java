package com.best_store.right_bite.dto.user;

import com.best_store.right_bite.model.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link User}
 */
public record DefaultUserInfoResponseDto(
        long id, String email,
        boolean isAccountNonExpired, boolean isAccountNonLocked,
        boolean isCredentialsNonExpired, boolean isEnabled,
        LocalDateTime createdAt, String firstName,
        String lastName, String imageUrl,
        String phoneNumber, String country,
        String city, String streetName, String houseNumber,
        String zipCode, Set<String> roles) implements Serializable, BaseUserInfo {
}