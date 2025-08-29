package com.best_store.right_bite.controller.search;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.search.SrchRequest;
import com.best_store.right_bite.service.search.Search;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/look")
@RequiredArgsConstructor
@Validated
public class SrchController {
    private final Search searchService;
    @PostMapping("/search")
    public List<Integer> search(@RequestBody @Valid SrchRequest srchRequest) {
        return searchService.search(srchRequest);
    }
}
