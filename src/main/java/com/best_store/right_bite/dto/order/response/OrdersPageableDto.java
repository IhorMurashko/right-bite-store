package com.best_store.right_bite.dto.order.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record OrdersPageableDto(
        @ArraySchema(arraySchema = @Schema(description = "Orders on the current page"))
        List<OrderResponseDto> orders,
        @Schema(description = "Current page number (0-based)", example = "0")
        int pageNo,
        @Schema(description = "Page size", example = "10")
        int pageSize,
        @Schema(description = "Total number of elements", example = "25")
        long totalElements,
        @Schema(description = "Total number of pages", example = "3")
        int totalPages,
        @Schema(description = "Is this the last page", example = "false")
        boolean lastPage
) {
}
