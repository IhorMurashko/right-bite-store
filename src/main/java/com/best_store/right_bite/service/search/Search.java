package com.best_store.right_bite.service.search;

import com.best_store.right_bite.dto.search.SrchRequest;
import com.best_store.right_bite.service.catalog.ProductServiceImpl;
import com.best_store.right_bite.service.search.luceneIndexer.Indexer;
import com.best_store.right_bite.service.search.luceneIndexer.Run;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class Search implements SrchInterface {

    private final ProductServiceImpl catalogService;

    @Override
    public ArrayList<Integer> search(SrchRequest srchRequest) {
        ArrayList<Integer> result = new ArrayList<>();
        boolean triggerIndexing = true; // This should be set based on your logic to determine if indexing is needed
        log.info("Search request received: {}", srchRequest);
        try {
            Indexer.indexFoods(catalogService.getAllProduct());
            log.info("Indexing completed successfully.");
            return Run.search(srchRequest.keyWord());
        } catch (IOException | ParseException e) {
            log.error("Search error", e);
            return result;
        }
    }
}

