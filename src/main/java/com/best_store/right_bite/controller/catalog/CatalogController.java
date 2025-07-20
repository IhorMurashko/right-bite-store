package com.best_store.right_bite.controller.catalog;


import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductFilterRequest;

import com.best_store.right_bite.service.catalog.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Catalog contorller",
        description = "it is letting get information about product, its brands, categories and filter by parameters - price, desk, ask, brand, category."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CatalogController {

    private final CatalogService catalogService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping()
    @Operation(
            summary = "getting all products without filters",
            description = "no parameters, default realization of getting all products"
    )
    public List<ProductDTO> getAllProduct()
    {
        return catalogService.getAllProduct();
    }


    @Operation(
            summary = "getting all products with filters",
            description = "parametrized, "
    )
    @PostMapping("/filter")
    public Page<ProductDTO> getAllProduct(
            @RequestBody(required = false) ProductFilterRequest productFilterRequest)
    {
        return catalogService.getAllProductPageable(productFilterRequest);
    }

    @GetMapping("/category")
    public List<CategoryDTO> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/brand")
    public List<BrandDTO> getAllBrand() {
        return brandService.getAllBrands();
    }

}
