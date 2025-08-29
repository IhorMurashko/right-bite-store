package com.best_store.right_bite.service.serviceBMI;

import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@FunctionalInterface
public interface BMIService {
    BmiResponse calculateBmi(@Valid @NotNull BmiRequest bmiRequest);
}
