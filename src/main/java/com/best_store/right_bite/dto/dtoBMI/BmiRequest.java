package com.best_store.right_bite.dto.dtoBMI;


import com.best_store.right_bite.constant.bmi.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * BmiRequest is a data transfer object that represents the request for calculating BMI.
 * @param age age of the person in years
 * @param gender gender of person MALE FEMAlE
 * @param weightKg weight of the person in kilograms
 * @param heightCm height of the person in centimeters
 */
public record BmiRequest(
        @NotNull
        @Min(value = 2, message = "Age must be greater than 1 year")
        @Max(value = 120, message = "Age must be less than 120 years")
        @Schema(description = "Users age", example = "18", accessMode = Schema.AccessMode.READ_ONLY)
        int age,
        @NotNull
        @Schema(description = "Choose gender using radiobutton", example = "MALE", accessMode = Schema.AccessMode.READ_ONLY)
        Gender gender,
        @NotNull
        @Min(value = 2, message = "Weight must be greater than 1 kg")
        @Max(value = 250, message = "Weight must be less than 250 kg")
        @Schema(description = "Users weight in kg", example = "77.0", accessMode = Schema.AccessMode.READ_ONLY)
        double weightKg,
        @NotNull
        @Min(value = 2, message = "Height must be greater than 1 cm")
        @Max(value = 250, message = "Height must be less than 250 cm")
        @Schema(description = "Users height in cm", example = "184.0", accessMode = Schema.AccessMode.READ_ONLY)
        double heightCm
) {
}

