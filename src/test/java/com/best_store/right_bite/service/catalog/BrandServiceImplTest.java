package com.best_store.right_bite.service.catalog;

import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.mapper.catalog.BrandMapper;
import com.best_store.right_bite.model.catalog.Brand;
import com.best_store.right_bite.repository.catalog.BrandRepository;
import com.best_store.right_bite.service.catalog.brandDomain.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

        Mockito.when(brandRepository.findAll()).thenReturn(getArrayBrandListEntity());

        List<Brand> brands = getArrayBrandListEntity();
        List<BrandDTO> dtos = getArrayBrandListDTO();

        for (int i = 0; i < brands.size(); i++) {
            Mockito.when(brandMapper.toDTO(brands.get(i))).thenReturn(dtos.get(i));
        }


        List<BrandDTO> allBrands = brandService.getAllBrands();

        assertNotNull(allBrands);
        assertEquals(6, allBrands.size());
        assertEquals("test", allBrands.get(0).getBrandName());

    }

    @Test
    void getAllBrandsOnEmptyResult() {
        Mockito.when(brandRepository.findAll()).thenReturn(new ArrayList<>());

        List<BrandDTO> allBrands = brandService.getAllBrands();

        assertNotNull(allBrands);
        assertEquals(0, allBrands.size());
    }


    private List<BrandDTO> getArrayBrandListDTO() {
        return List.of(
                new BrandDTO("test"),
                new BrandDTO("test2"),
                new BrandDTO("test3"),
                new BrandDTO("test4"),
                new BrandDTO("test5"),
                new BrandDTO("test6")
        );
    }

    private List<Brand> getArrayBrandListEntity() {
        return List.of(
                new Brand("test"),
                new Brand("test2"),
                new Brand("test3"),
                new Brand("test4"),
                new Brand("test5"),
                new Brand("test6")
        );
    }


}