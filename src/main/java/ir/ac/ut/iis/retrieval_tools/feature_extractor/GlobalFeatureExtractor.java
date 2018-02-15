/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.feature_extractor;

import ir.ac.ut.iis.retrieval_tools.classifier.Calculator;
import java.io.File;
import java.io.IOException;
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
public class GlobalFeatureExtractor extends FeatureExtractor {

    private IndexReader reader;

    public GlobalFeatureExtractor(String indexFolder) {
        try {
            Directory index = FSDirectory.open(new File(indexFolder).toPath());
            reader = DirectoryReader.open(index);
        } catch (IOException ex) {
            Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected double df(String term) {
        double df;
        try {
            df = reader.docFreq(new Term("text", term));
        } catch (IOException ex) {
            Logger.getLogger(GlobalFeatureExtractor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        if (df < 10) {
            return 1000.;
        } else {
            return Math.log(df + 3);
        }
    }

}
