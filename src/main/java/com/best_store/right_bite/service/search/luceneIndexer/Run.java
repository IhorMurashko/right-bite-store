package com.best_store.right_bite.service.search.luceneIndexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Run {
    public static ArrayList<Integer> search(String queryStr) throws ParseException, IOException {
        ArrayList<Integer> result = new ArrayList<>();
        Analyzer analyzer = new StandardAnalyzer();
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indexDir")));
        IndexSearcher searcher = new IndexSearcher(reader);

        String[] fields = {"id", "productName", "description"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
        Query query = parser.parse(queryStr);

        TopDocs res = searcher.search(query, 10);

        for (ScoreDoc scoreDoc : res.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String idStr = doc.get("id");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    result.add(id);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ID format: " + idStr);
                }
            }
        }

        reader.close();
        return result;
    }
}
