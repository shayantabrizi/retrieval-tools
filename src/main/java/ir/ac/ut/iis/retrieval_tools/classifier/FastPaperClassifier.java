/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.classifier;

import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import ir.ac.ut.iis.retrieval_tools.domain.MyIterable;
import ir.ac.ut.iis.retrieval_tools.papers.BasePaper;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shayan
 */
public class FastPaperClassifier implements MyIterable<BasePaper>, Closeable {

    Calculator jsd;
    Writer writer;
    LMExtractor lme;

    public FastPaperClassifier(LMExtractor lme, Calculator jsd, Writer writer) {
        this.jsd = jsd;
        this.writer = writer;
        this.lme = lme;
    }

    @Override
    public boolean doAction(BasePaper d) {
        Map<String, Map<String, Double>> p = new TreeMap<>();
        for (String s : lme.getMap().keySet()) {
            p.put(s, lme.getFeatures(d.getTitle().trim() + " " + d.getAbs().trim(), s));
        }
        try {
            writer.write(d.getId() + " " + d.getDocId() + "::");
            System.out.println(d.getId() + " " + d.getDocId() + "::");
            for (String topic : lme.getMap().keySet()) {
                ((Calculator.FastPerTopicSimilarityCalculator_KL) jsd.getSc()).setToBeClassified(p);
                Calculator.MaxResult max = jsd.calculateSimilarity(null, new Topic(topic, 0));
                String s = "\"" + topic + "\": " + max.getSimilarity() + ";";
                writer.write(s);
                writer.flush();
//                System.out.print(s);
            }
            writer.write("\n");
//            System.out.println("");
        } catch (IOException ex) {
            Logger.getLogger(FastPaperClassifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FastPaperClassifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

}
