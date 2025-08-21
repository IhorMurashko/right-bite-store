package com.best_store.right_bite.controller.bmi;

import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import com.best_store.right_bite.service.serviceBMI.BMIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
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

    @PostMapping("/calculate")
    @Operation(
            summary     = "Calculate BMI",
            description = "Calculates BMI based on age, gender, weight and height.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = BmiRequest.class),
                            examples  = @ExampleObject(
                                    name     = "BmiRequestExample",
                                    summary  = "Example of a BMI request",
                                    value    = "{ \"age\": 25, \"gender\": \"MALE\", \"weightKg\": 70.0, \"heightCm\": 175.0 }"
                            )
                    )
            )
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "BMI calculated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BmiResponse.class),
                            examples = @ExampleObject(
                                    name = "BmiResponseExample",
                                    summary = "Example of a successful BMI calculation response",
                                    value = "{ \"bmiValue\": 22.5, \"category\": \"Normal weight\", \"suggestions\": [\"Eat balanced meals\", \"Stay hydrated\"] }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data.sql",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BmiErrorExample",
                                    summary = "Error response for invalid input",
                                    value = "{ \"error\": \"Invalid age provided\" }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "BmiServerErrorExample",
                                    summary = "Error response for server issues",
                                    value = "{ \"error\": \"An unexpected error occurred\" }"
                            )
                    )
            )
    })
    @PreAuthorize("permitAll()")
    public ResponseEntity<BmiResponse> calculate(@RequestBody @Valid BmiRequest bmiRequest) {
        BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);
        return new ResponseEntity<>(bmiResponse, HttpStatus.OK);
    }

}
