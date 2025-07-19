package com.best_store.right_bite.controller.catalog;


import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductFilterRequest;

import com.best_store.right_bite.service.catalog.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CatalogController {

    private final CatalogService catalogService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping()
    public List<ProductDTO> getAllProduct()
    {
        return catalogService.getAllProduct();
    }

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
