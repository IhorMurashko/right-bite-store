package com.best_store.right_bite.controller.search;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import com.best_store.right_bite.dto.search.SrchRequest;
import com.best_store.right_bite.service.search.Search;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/look")
@RequiredArgsConstructor
@Validated
@Tag(name = "Search Controller", description = "Input word that is somehow connected to the product you are searching for.")
public class SrchController {

    private final Search searchService;

    @Operation(
            summary = "Search for any products",
            description = "You need to provide a word or a couple to search",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SrchRequest.class)
                    ),
                    description = "char* type"
            )
    )
    @PostMapping("/search")
    public List<Integer> search(@RequestBody @Valid SrchRequest srchRequest) {
        return searchService.search(srchRequest);
    }
}
