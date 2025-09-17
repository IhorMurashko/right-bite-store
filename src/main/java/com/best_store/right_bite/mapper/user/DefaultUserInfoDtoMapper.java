package com.best_store.right_bite.mapper.user;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.user.User;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps {@link User} entities to {@link DefaultUserInfoResponseDto}.
 * <p>
 * Used to expose user data in API responses in a consistent format.
 * <p>
 * Mapping is one-way only (entity → DTO).
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DefaultUserInfoDtoMapper {

    /**
     * Converts a {@link User} entity to a {@link DefaultUserInfoResponseDto}.
     *
     * @param user the User entity
     * @return mapped DefaultUserInfoResponseDto
     */
    @Mapping(target = "roles", source = "roles", qualifiedByName = "getUserRoles")
    DefaultUserInfoResponseDto toDTO(User user);

    @Named("getUserRoles")
    default Set<String> getUserRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

    default Set<Role> map(Set<String> value) {
        return Collections.emptySet();
    }
}