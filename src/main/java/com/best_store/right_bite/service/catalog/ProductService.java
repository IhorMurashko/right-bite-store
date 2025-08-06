package com.best_store.right_bite.service.catalog;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductFilterRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProduct();
    Page<ProductDTO> getAllProductPageable(ProductFilterRequest productFilterRequest);
    ProductDTO getProductDTOById(Long id);
}
