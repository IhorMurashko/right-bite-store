package com.best_store.right_bite.controller;

import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import com.best_store.right_bite.service.serviceBMI.BMIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class BmiContoller {
    private final BMIService bmiService;

    @PostMapping("/calculate")
    public ResponseEntity<BmiResponse> calculate(@RequestBody @Valid BmiRequest bmiRequest) {
        BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);
        return new ResponseEntity<>(bmiResponse, HttpStatus.OK);
    }

}
