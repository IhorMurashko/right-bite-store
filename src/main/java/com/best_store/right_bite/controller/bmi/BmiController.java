package com.best_store.right_bite.controller.bmi;

import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import com.best_store.right_bite.service.serviceBMI.BMIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling BMI calculations.
 * This controller provides an endpoint to calculate BMI based on user input.
 */
@RestController
@RequestMapping("/api/v1/bmi")
@RequiredArgsConstructor
@Validated
@Tag(name = "BmiController", description = "Controller which calculates your BMI. Gives you suggestions about your food and recommends our products.")
public class BmiController {

    private final BMIService bmiService;

    @Operation(
            summary = "Calculate BMI and Get Personalized Recommendations",
            description = "Calculates BMI based on provided measurements and returns personalized health insights with product recommendations. " +
                    "The calculation takes into account age (2-120 years), gender, weight (2-250 kg), and height (2-250 cm).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BmiRequest.class)
                    ),
                    description = "User's physical measurements and characteristics required for BMI calculation"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BMI successfully calculated with personalized recommendations",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BmiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input parameters",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)
                            )
                    )
            }
    )
    @PostMapping("/calculate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BmiResponse> calculate(@RequestBody @Valid @NotNull BmiRequest bmiRequest) {
        BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);
        return new ResponseEntity<>(bmiResponse, HttpStatus.OK);
    }
}
