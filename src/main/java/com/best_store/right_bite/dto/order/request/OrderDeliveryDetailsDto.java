package com.best_store.right_bite.dto.order.request;

import com.best_store.right_bite.constant.constraint.user.UserFieldsConstraint;
import com.best_store.right_bite.constant.order.DeliveryMethod;
import com.best_store.right_bite.dto.order.assing.OrderDeliveryDetailsAssigner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.best_store.right_bite.model.order.OrderDeliveryDetails}
 */
public record OrderDeliveryDetailsDto(
        @Schema(description = "Recipient's first name", example = "John")
        @NotNull
        @NotBlank
        @Pattern(regexp = UserFieldsConstraint.NAME_PATTERN,
        message = UserFieldsConstraint.NAME_MESSAGE)
        String firstname,
        @Schema(description = "Recipient's last name", example = "Doe")
        String lastname,
        @Schema(description = "Contact phone in international format", example = "+15551234567")
        @NotNull
        @NotBlank
        @Pattern(regexp = UserFieldsConstraint.PHONE_PATTERN,
        message = UserFieldsConstraint.PHONE_MESSAGE)
        String phoneNumber,
        @Schema(description = "House or apartment number", example = "221B")
        String houseNumber,
        @Schema(description = "Street name", example = "Baker Street")
        String streetName,
        @Schema(description = "City", example = "London")
        String city,
        @Schema(description = "Country", example = "UK")
        String country,
        @Schema(description = "Postal/ZIP code", example = "NW1 6XE")
        String zipCode,
        @Schema(description = "Optional delivery comment", example = "Ring the bell twice")
        String comment,
        @Schema(description = "Delivery method")
        @NotNull
        DeliveryMethod deliveryMethod) implements OrderDeliveryDetailsAssigner, Serializable {
}