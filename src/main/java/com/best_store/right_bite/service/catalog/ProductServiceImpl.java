package com.best_store.right_bite.service.catalog;


import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductFilterRequest;
import com.best_store.right_bite.dto.catalog.ProductSalesDTO;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.catalog.ProductNotFoundException;
import com.best_store.right_bite.mapper.catalog.ProductMapper;
import com.best_store.right_bite.repository.catalog.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository catalogRepository;

    private final ProductMapper productEntityToDTOMapper;

    @Override
    public List<ProductDTO> getAllProduct() {
        return catalogRepository.findAll().stream().map(productEntityToDTOMapper::toDTO).toList();
    }

    @Override
    public ProductDTO getProductDTOById(Long id) {
        return productEntityToDTOMapper.toDTO(catalogRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(ExceptionMessageProvider.ID_PRODUCT_NOT_FOUND.formatted(id))));
    }

    @Override
    public Page<ProductDTO> getAllProductPageable(ProductFilterRequest productFilterRequest) {
        List<ProductDTO> allProduct = getAllProduct();

        List<ProductDTO> filtered = applyFilters(allProduct, productFilterRequest);
        applySorting(filtered, productFilterRequest);

        return paginate(filtered, productFilterRequest.getPage(), productFilterRequest.getSize());
    }

    private List<ProductDTO> applyFilters(List<ProductDTO> products, ProductFilterRequest f) {
        return products.stream()
                .filter(p -> f.getAZ() == null ||
                        Character.toLowerCase(p.getProductName().trim().charAt(0)) == Character.toLowerCase(f.getAZ())
                )
                .filter(p -> f.getCategoryName() == null || p.getCategories().stream()
                        .anyMatch(c -> c.getCategoryName().equalsIgnoreCase(f.getCategoryName())))
                .filter(p -> f.getBrand() == null || p.getBrand().getBrandName().equalsIgnoreCase(f.getBrand()))
                .filter(p -> f.getPriceFrom() == null || p.getPrice().compareTo(f.getPriceFrom()) >= 0)
                .filter(p -> f.getPriceTo() == null || p.getPrice().compareTo(f.getPriceTo()) <= 0)
                .collect(Collectors.toList());
    }

    private void applySorting(List<ProductDTO> products, ProductFilterRequest f) {
        String sortBy = f.getSortBy();
        Comparator<ProductDTO> comparator = null;

        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc" -> comparator = Comparator.comparing(p -> p.getPrice());
                case "priceDesc" -> comparator = Comparator.comparing((ProductDTO p) -> p.getPrice()).reversed();
                case "rating" -> comparator = Comparator.comparing((ProductDTO p) -> p.getRating()).reversed();
                case "popular" -> comparator = Comparator.comparing((ProductDTO p) -> p.getProductSales().stream()
                        .mapToInt(ProductSalesDTO::getQuantity)
                        .max()
                        .orElse(0)
                ).reversed();
            }
        }

        if (comparator != null) {
            products.sort(comparator);
        }
    }


    private Page<ProductDTO> paginate(List<ProductDTO> products, int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, products.size());

        if (fromIndex > products.size()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), products.size());
        }

        List<ProductDTO> pageContent = products.subList(fromIndex, toIndex);
        return new PageImpl<>(pageContent, PageRequest.of(page, size), products.size());
    }

}
