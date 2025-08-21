package com.best_store.right_bite.controller.catalog;


import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductFilterRequest;

import com.best_store.right_bite.service.catalog.brandDomain.BrandService;
import com.best_store.right_bite.service.catalog.categoryDomain.CategoryService;
import com.best_store.right_bite.service.catalog.productDomain.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    private final ProductService catalogService;
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
            summary = "Get product by ID",
            description = "Returns a product by its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully returned"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return catalogService.getProductDTOById(id);
    }


    @Operation(
            summary = "getting all products with filters",
            description = "parametrized, returning sorted product",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Filtering parameters for searching products",
                    required = false,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductFilterRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered product list successfully returned")
            }
    )
    @PostMapping("/filter")
    public Page<ProductDTO> getAllProduct(
            @RequestBody(required = false) ProductFilterRequest productFilterRequest)
    {
        return catalogService.getAllProductPageable(productFilterRequest);
    }


    @Operation(
            summary = "getting all category",
            description = "no parameters, default realization of getting all category"
    )
    @GetMapping("/category")
    public List<CategoryDTO> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @Operation(
            summary = "getting all brands",
            description = "no parameters, default realization of getting all brands"
    )
    @GetMapping("/brand")
    public List<BrandDTO> getAllBrand() {
        return brandService.getAllBrands();
    }



}
