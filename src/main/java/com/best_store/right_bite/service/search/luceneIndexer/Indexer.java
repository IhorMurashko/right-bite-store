package com.best_store.right_bite.service.search.luceneIndexer;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Indexer {
    public static boolean indexFoods(List<ProductDTO> foods) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        Directory directory = FSDirectory.open(Paths.get("indexDir"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try(IndexWriter writer = new IndexWriter(directory,config)){
            for (ProductDTO food : foods) {
                Document doc = new Document();
                doc.add(new TextField("id", food.getId().toString(), TextField.Store.YES));
                doc.add(new TextField("productName", food.getProductName(), TextField.Store.YES));
                doc.add(new TextField("description", food.getDescription(), TextField.Store.YES));
                writer.addDocument(doc);
            }
        }
        return false;
    }
}
