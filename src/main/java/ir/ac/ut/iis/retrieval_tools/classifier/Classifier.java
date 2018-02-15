/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.classifier;

import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import ir.ac.ut.iis.retrieval_tools.crawler.Crawler;
import ir.ac.ut.iis.retrieval_tools.crawler.Parser;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shayan
 */
public class Classifier extends Crawler<Topic> {

    final Calculator jsd;
    final String toBeClassified;
    final Writer output;
    public static final String azimiDatasetHome = "/home/shayan/Desktop/Azimi/";

    public Classifier(Parser parser, Calculator jsd, String toBeClassified, Writer output) throws IOException {
        super(parser);
        this.jsd = jsd;
        this.toBeClassified = toBeClassified;
        this.output = output;
    }

    @Override
    public boolean doAction(File fileEntry, Topic t) {
        Calculator.MaxResult maxResult = null;
        try {
            List<String> topics = getTopics(t);
            jsd.fe1.setTopic(topics);
            jsd.fe2.setTopic(topics);
            maxResult = jsd.calculateSimilarity(toBeClassified, t);
        } catch (Exception ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
//        String s = t.getTopicId() + " \"" + t.getTopic() + "\" " + maxResult.getSimilarity() + " \"" + (maxResult.getDoc() != null ? maxResult.getDoc().getText().replace("\r\n", " ").replace("\n", " ").replace("\t", " ").replace("\"", " ") : null) + "\"\n";
        String s = "\"" + t.getTopic() + "\": " + maxResult.getSimilarity() + ";";
        try {
            output.write(s);
        } catch (IOException ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
        System.out.print(s);
        return true;
    }

    protected List<String> getTopics(Topic t) {
        List<String> topics = new LinkedList<>();
        topics.add(t.getTopic());
        return topics;
    }

    @Override
    public void close() throws IOException {
    }
}
