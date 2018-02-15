/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.feature_extractor;

import ir.ac.ut.iis.retrieval_tools.azimi.TaxonomyHierarchyReader;
import ir.ac.ut.iis.retrieval_tools.classifier.Calculator;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author shayan
 */
public class LocalFeatureExtractor extends FeatureExtractor {

    final private Map<String, IndexReader> readers = new TreeMap<>();
    final private TaxonomyHierarchyReader thr;
    final private static Map<String, Map<String, Integer>> DFcache = new TreeMap<>();
    final private static Map<String, Map<String, Long>> TFcache = new TreeMap<>();

    public LocalFeatureExtractor(String rootFolder, TaxonomyHierarchyReader thr) {
        try {
            for (TaxonomyHierarchyReader.Node t : thr.getTopics(1)) {
                Directory index = FSDirectory.open(new File(rootFolder + t.getNodeName()).toPath());
                readers.put(t.getNodeName(), DirectoryReader.open(index));
            }
        } catch (IOException ex) {
            Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        this.thr = thr;
    }

    @Override
    protected double df(String term) {
        long count = 0;
        long inTopicCount = 0;
        Map<String, Integer> get = DFcache.get(term);
        Map<String, Long> get2 = TFcache.get(term);
        if (get == null) {
            long total = 0;
            get = new TreeMap<>();
            get2 = new TreeMap<>();
            for (Map.Entry<String, IndexReader> e : readers.entrySet()) {
                Integer g;
                Long g2;
                try {
                    g = e.getValue().docFreq(new Term("text", term));
                    g2 = e.getValue().totalTermFreq(new Term("text", term));
                } catch (IOException ex) {
                    Logger.getLogger(LocalFeatureExtractor.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
                get.put(e.getKey(), g);
                get2.put(e.getKey(), g2);
                total += g2;
            }
            if (total > 3) {
                DFcache.put(term, get);
                TFcache.put(term, get2);
            }
        }

        for (Map.Entry<String, IndexReader> e : readers.entrySet()) {
            String topic = e.getKey();

            boolean check = false;
            for (String t : getTopic()) {
                if (thr.getByTopicName(t).getAncestorsAsString(1).contains(topic)) {
                    check = true;
                }
            }
            if (!check) {
                count += get.get(topic);
            } else {
                inTopicCount += get2.get(topic);
            }
        }
        if (inTopicCount < 10) {
            return 100.;
        } else {
            return Math.log(count + 3);
        }
    }

}
