/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.classifier;

import ir.ac.ut.iis.retrieval_tools.azimi.SampleDocument;
import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import ir.ac.ut.iis.retrieval_tools.crawler.Parser;
import ir.ac.ut.iis.retrieval_tools.domain.MyIterable;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.FeatureExtractor;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shayan
 */
public class TopicClassifier implements MyIterable<Topic>, Closeable {

    FeatureExtractor localFeatureExtractor;
    Calculator jsd;
    Writer writer;
    int minCount;
    int maxCount;
    private int count = 0;

    public TopicClassifier(FeatureExtractor localFeatureExtractor, Calculator jsd, Writer writer) {
        this.localFeatureExtractor = localFeatureExtractor;
        this.jsd = jsd;
        this.writer = writer;
    }

    @Override
    public boolean doAction(Topic t) {
        StringBuilder sb = new StringBuilder();
        for (SampleDocument d : t.getDocs()) {
            sb.append(d.getText()).append("\n");
        }
        count++;
        if (minCount > count) {
            return true;
        }
        if (maxCount <= count) {
            return true;
        }
        System.out.println(t.getTopic() + " " + new Date());

        try (MyClassifier cl = new MyClassifier(new Topic.TopicParser(), jsd, sb.toString(), writer, t.getTopic())) {
            writer.write(t.getTopic() + "::");
            cl.crawl("/media/truecrypt1/Archieve/Research/Datasets/Azimi/dataset-cleaned/3");
            writer.write("\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(TopicClassifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(TopicClassifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

    private class MyClassifier extends Classifier {

        String docTopic;

        MyClassifier(Parser parser, Calculator jsd, String toBeClassified, Writer output, String docTopic) throws IOException {
            super(parser, jsd, toBeClassified, output);
            this.docTopic = docTopic;
        }

        @Override
        protected List<String> getTopics(Topic t) {
            List<String> topics = super.getTopics(t);
            topics.add(docTopic);
            return topics;
        }

        @Override
        public boolean doAction(File fileEntry, Topic t) {
            if (docTopic.compareTo(t.getTopic()) < 0) {
                return super.doAction(fileEntry, t);
            }
            return true;
        }

    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

}
