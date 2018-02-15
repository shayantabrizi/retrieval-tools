/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.azimi;

import ir.ac.ut.iis.retrieval_tools.classifier.Classifier;
import java.io.IOException;
import org.apache.lucene.search.similarities.BM25Similarity;

/**
 *
 * @author shayan
 */
public class IndexBuilder {

    public static void main(String[] args) throws IOException {
//        singleIndexBuilder();
//        multiIndexBuilder();
        highLevelIndexBuilder();
    }

    public static void singleIndexBuilder() throws IOException {
        Indexer indexer = new Indexer("index/", new BM25Similarity(), true);
        try (TaxonomyIndexer tr = new TaxonomyIndexer(new Topic.TopicParser(), indexer)) {
            tr.crawl("/home/shayan/Desktop/Azimi/dataset-cleaned/3");
        }
    }

    public static void multiIndexBuilder() throws IOException {

        try (TaxonomyPerTopicIndexer tr = new TaxonomyPerTopicIndexer(new Topic.TopicParser(), "index/multi/")) {
            tr.crawl("/home/shayan/Desktop/Azimi/dataset-cleaned/3");
        }
    }

    public static void highLevelIndexBuilder() throws IOException {

        try (TaxonomyPerHighLevelTopicIndexer tr = new TaxonomyPerHighLevelTopicIndexer(new Topic.TopicParser(), "index/multi/1/", new TaxonomyHierarchyReader(Classifier.azimiDatasetHome + "taxonomy2012.txt"))) {
            tr.crawl("/home/shayan/Desktop/Azimi/dataset-cleaned/3");
        }
    }

}
