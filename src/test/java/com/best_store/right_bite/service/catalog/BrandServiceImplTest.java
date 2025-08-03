package com.best_store.right_bite.service.catalog;

import com.best_store.right_bite.mapper.catalog.BrandMapper;
import com.best_store.right_bite.repository.catalog.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {


    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    void getAllBrandsOnSuccessfulResult() {
    }



}