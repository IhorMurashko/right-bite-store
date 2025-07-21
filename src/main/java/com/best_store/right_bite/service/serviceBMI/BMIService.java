package com.best_store.right_bite.service.serviceBMI;

import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import jakarta.validation.Valid;

public interface BMIService {
    BmiResponse calculateBmi(@Valid BmiRequest bmiRequest);
}
