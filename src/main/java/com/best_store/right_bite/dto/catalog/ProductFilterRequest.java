package com.best_store.right_bite.dto.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "The object of product filtering")
public class ProductFilterRequest {


    @Schema(description = "category of product", example = "Хлебобулочные изделия")
    private String categoryName;

    @Schema(description = "brand of product", example = "Biona")
    private String brand;

    @Schema(description = "range price from ", example = "10")
    private BigDecimal priceFrom;

    @Schema(description = "range price to ", example = "25")
    private BigDecimal priceTo;

    @Schema(description = "sort by first letter", example = "х")
    @JsonProperty("aZ")
    private Character aZ;

    @Schema(description = "sort by parameters", example = "priceAsc, priceDesc, rating, popular")
    private String sortBy; // "priceAsc", "priceDesc", "rating", "popular"

    @Schema(description = "Number page", example = "2")
    private int page = 0;

    @Schema(description = "count of items in page", example = "10")
    private int size = 24;

}
