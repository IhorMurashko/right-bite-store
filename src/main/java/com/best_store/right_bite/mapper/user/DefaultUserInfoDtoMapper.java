package com.best_store.right_bite.mapper.user;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Maps {@link User} entities to {@link DefaultUserInfoResponseDto}.
 * <p>
 * Used to expose user data in API responses in a consistent format.
 *
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
    DefaultUserInfoResponseDto toDTO(User user);
}

