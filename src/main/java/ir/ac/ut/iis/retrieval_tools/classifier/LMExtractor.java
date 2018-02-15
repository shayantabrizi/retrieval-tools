/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.classifier;

import ir.ac.ut.iis.retrieval_tools.Config;
import ir.ac.ut.iis.retrieval_tools.azimi.SampleDocument;
import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import static ir.ac.ut.iis.retrieval_tools.classifier.Calculator.tokenizeString;
import ir.ac.ut.iis.retrieval_tools.crawler.Crawler;
import ir.ac.ut.iis.retrieval_tools.crawler.Parser;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.FeatureExtractor;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.LocalFeatureExtractor;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

/**
 *
 * @author Shayan
 */
public class LMExtractor extends Crawler<Topic> {

    final FeatureExtractor localFeatureExtractor;
    Map<String, Map<String, Double>> map = new TreeMap<>();

    public LMExtractor(Parser parser, LocalFeatureExtractor localFeatureExtractor) {
        super(parser);
        this.localFeatureExtractor = localFeatureExtractor;
    }

    @Override
    public boolean doAction(File fileEntry, Topic t) {
        StringBuilder sb = new StringBuilder();
        for (SampleDocument d : t.getDocs()) {
            sb.append(d.getText()).append("\n");
        }
        String s = sb.toString();
        Map<String, Double> features = getFeatures(s, t.getTopic());
        map.put(t.getTopic(), features);
        System.out.println(t.getTopic());
        return true;
    }
    
    public Map<String, Double> getFeatures(String s, String t) {
        List<String> list = new LinkedList<>();
        list.add(t);
        return getFeatures(s, list);
    }
    public Map<String, Double> getFeatures(String s, List<String> t) {
        List<String> s1Tokens;
        try (Analyzer analyzer = new EnglishAnalyzer(CharArraySet.EMPTY_SET)) {
            s1Tokens = tokenizeString(analyzer, s);
        }
        s1Tokens.removeAll(Config.stopWords);
        localFeatureExtractor.setTopic(t);
        Map<String, Double> features = localFeatureExtractor.getFeature(s1Tokens);
        return features;
    }

    public Map<String, Map<String, Double>> getMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void close() {
    }
}
