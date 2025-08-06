//package com.best_store.right_bite.controller.catalog;
//
//import com.best_store.right_bite.dto.catalog.ProductDTO;
//import com.best_store.right_bite.service.catalog.BrandService;
//import com.best_store.right_bite.service.catalog.CatalogService;
//import com.best_store.right_bite.service.catalog.CategoryService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//@WebMvcTest(controllers = {CatalogController.class},  excludeAutoConfiguration = {
//        SecurityAutoConfiguration.class,
//        SecurityFilterAutoConfiguration.class})
//@Import({MockitoBeansConfig.class})
//@AutoConfigureMockMvc(addFilters = false)
//class CatalogControllerTest {
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private CatalogService catalogService;
//
//    @Autowired
//    private CategoryService categoryService;
//
//    @Autowired
//    private BrandService brandService;
//
//
//    @Test
//    void getAllProduct_shouldReturnListOfProducts() throws Exception {
//        ProductDTO product1 = DataFactoryTest.createProductWithCustomName("Product1");
//        ProductDTO product2 = DataFactoryTest.createProductWithCustomName("Product2");
//
//        //given
//        Mockito.when(catalogService.getAllProduct()).thenReturn(List.of(product1, product2));
//
//
//        mockMvc.perform(get("/api/v1/catalog")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].name").value("Test Product"));
//
//
//    }
//
//    @Test
//    void testGetAllProduct() {
//    }
//
//    @Test
//    void getAllCategory() {
//    }
//
//    @Test
//    void getAllBrand() {
//    }
//
//
//}