/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.feature_extractor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shayan
 */
public abstract class FeatureExtractor {

    private List<String> topic;

    public HashMap<String, Double> getFeature(List<String> tokens) {
        HashMap<String, Integer> numOfwords = new HashMap<>();
        for (String word : tokens) {
            if (numOfwords.keySet().contains(word)) {
                numOfwords.put(word, numOfwords.get(word) + 1);
            } else {
                numOfwords.put(word, 1);
            }
        }

        HashMap<String, Double> normalizedWordCounts = new HashMap<>();
        double totalScores = 0;
        for (Map.Entry<String, Integer> e : numOfwords.entrySet()) {
            double normalizedWordCount;
            normalizedWordCount = e.getValue() / df(e.getKey());
            totalScores += normalizedWordCount;
            normalizedWordCounts.put(e.getKey(), normalizedWordCount);
        }
        HashMap<String, Double> wordFreqs = new HashMap<>();
        double temp = 0;
        for (Map.Entry<String, Double> e : normalizedWordCounts.entrySet()) {
            wordFreqs.put(e.getKey(), e.getValue() / totalScores);
            temp += e.getValue() / totalScores;

        }
//        System.out.println("-----------------------------------------" + temp);
        return wordFreqs;
    }

    public List<String> getTopic() {
        return Collections.unmodifiableList(topic);
    }

    public void setTopic(String topic) {
        this.topic = new LinkedList<>();
        this.topic.add(topic);
    }
    public void setTopic(List<String> topic) {
        this.topic = topic;
    }

    protected abstract double df(String term);

}
