package com.best_store.right_bite.mapper.user;


import com.best_store.right_bite.dto.user.update.UserUpdateRequestDto;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import org.mapstruct.*;

/**
 * Maps and updates {@link User} entities using data from {@link UserUpdateRequestDto}.
 * <p>
 * Fields that are {@code null} in the DTO are ignored. Special transformation
 * logic is applied using {@link UserFieldAdapter} (e.g. normalization, sanitization).
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserFieldAdapter.class)
public interface UpdatableUserInfoMapper {

    /**
     * Updates the given {@link User} entity in-place using non-null fields
     * from the {@link UserUpdateRequestDto}.
     *
     * @param userUpdateRequestDto source DTO with updated values
     * @param user                 target User entity to be updated
     */
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
