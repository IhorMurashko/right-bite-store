package com.best_store.right_bite.controller.catalog;


import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.service.catalog.BrandService;
import com.best_store.right_bite.service.catalog.ProductService;
import com.best_store.right_bite.service.catalog.CategoryService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class MockitoBeansConfig {

    @Bean
    @Primary
    public ProductService catalogService() {
        return Mockito.mock(ProductService.class);
    }

    @Bean
    @Primary
    public CategoryService categoryService() {
        return Mockito.mock(CategoryService.class);
    }

    @Bean
    @Primary
    public BrandService brandService() {
        return Mockito.mock(BrandService.class);
    }

    @Bean
    @Primary
    public JwtProvider jwtProvider() {
        return Mockito.mock(JwtProvider.class);
    }
}
