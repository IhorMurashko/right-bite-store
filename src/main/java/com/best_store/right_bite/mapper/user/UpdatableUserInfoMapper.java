package com.best_store.right_bite.mapper.user;


import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserFieldAdapter.class)
public interface UpdatableUserInfoMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "email", qualifiedByName = "toLower")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "normalizeName")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "normalizeName")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "sanitizePhoneNumber")
    @Mapping(target = "country", source = "country", qualifiedByName = "normalizeAddress")
    @Mapping(target = "city", source = "city", qualifiedByName = "normalizeAddress")
    @Mapping(target = "streetName", source = "streetName", qualifiedByName = "normalizeAddress")
    @Mapping(target = "zipCode", source = "zipCode", qualifiedByName = "normalizeZipCode")
    void updateEntityFromDto(UserUpdateRequestDto userUpdateRequestDto, @MappingTarget User user);
}
