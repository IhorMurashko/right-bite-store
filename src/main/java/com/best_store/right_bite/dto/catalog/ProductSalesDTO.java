package com.best_store.right_bite.dto.catalog;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesDTO {
    Long productId;
    Integer quantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime saleDate;
}
