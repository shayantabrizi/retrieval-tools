/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.classifier;

import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import ir.ac.ut.iis.retrieval_tools.domain.MyIterable;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.FeatureExtractor;
import ir.ac.ut.iis.retrieval_tools.papers.BasePaper;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shayan
 */
public class PaperClassifier implements MyIterable<BasePaper>, Closeable {

    FeatureExtractor localFeatureExtractor;
    Calculator jsd;
    Writer writer;

    public PaperClassifier(FeatureExtractor localFeatureExtractor, Calculator jsd, Writer writer) {
        this.localFeatureExtractor = localFeatureExtractor;
        this.jsd = jsd;
        this.writer = writer;
    }

    @Override
    public boolean doAction(BasePaper d) {
        try (Classifier cl = new Classifier(new Topic.TopicParser(), jsd, d.getTitle() + " " + d.getAbs(), writer)) {
            writer.write(d.getId() + " " + d.getDocId() + "::");
            cl.crawl("/media/truecrypt1/Archieve/Research/Datasets/Azimi/dataset-cleaned/3");
            writer.write("\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(PaperClassifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PaperClassifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

}
