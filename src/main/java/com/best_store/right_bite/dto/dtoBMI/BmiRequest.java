package com.best_store.right_bite.dto.dtoBMI;


import com.best_store.right_bite.constant.bmi.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static com.best_store.right_bite.constant.constraint.bmi.BmiConstraints.*;

/**
 * BmiRequest is a data.sql transfer object that represents the request for calculating BMI.
 *
 * @param age      age of the person in years
 * @param gender   gender of person MALE FEMAlE
 * @param weightKg weight of the person in kilograms
 * @param heightCm height of the person in centimeters
 */
public record BmiRequest(
        @NotNull
        @Min(value = MIN_AGE, message = MIN_AGE_EXCEPTION_MESSAGE)
        @Max(value = MAX_AGE, message = MAX_AGE_EXCEPTION_MESSAGE)
        @Schema(description = "Users age", example = "18", accessMode = Schema.AccessMode.READ_ONLY)
        int age,
        @NotNull
        @Schema(description = "Choose gender using radiobutton", example = "MALE", accessMode = Schema.AccessMode.READ_ONLY)
        Gender gender,
        @NotNull
        @Min(value = MIN_WEIGHT, message = MIN_WEIGHT_EXCEPTION_MESSAGE)
        @Max(value = MAX_WEIGHT, message = MAX_WEIGHT_EXCEPTION_MESSAGE)
        @Schema(description = "Users weight in kg", example = "77.0", accessMode = Schema.AccessMode.READ_ONLY)
        double weightKg,
        @NotNull
        @Min(value = MIN_HEIGHT, message = MIN_HEIGHT_EXCEPTION_MESSAGE)
        @Max(value = MAX_HEIGHT, message = MAX_HEIGHT_EXCEPTION_MESSAGE)
        @Schema(description = "Users height in cm", example = "184.0", accessMode = Schema.AccessMode.READ_ONLY)
        double heightCm
) {
}

