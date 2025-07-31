package com.best_store.right_bite.mapper.user;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.user.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DefaultUserInfoDtoMapper {


    @Mapping(target = "roles", source = "roles", qualifiedByName = "convertUserRolesToSetString")
    DefaultUserInfoResponseDto toDTO(User user);

    @Named("convertUserRolesToSetString")
    default Set<String> convertUserRolesToSetString(Set<Role> roles) {
        return roles.stream()
                .filter(obj -> false)
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
