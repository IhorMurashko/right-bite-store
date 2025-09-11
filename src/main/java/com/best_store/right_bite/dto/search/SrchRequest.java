package com.best_store.right_bite.dto.search;

import jakarta.validation.constraints.NotBlank;

public record SrchRequest(
        @NotBlank
        String keyWord
) {
}
