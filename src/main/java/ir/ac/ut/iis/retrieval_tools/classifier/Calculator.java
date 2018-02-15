/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.classifier;

import ir.ac.ut.iis.retrieval_tools.Config;
import ir.ac.ut.iis.retrieval_tools.azimi.SampleDocument;
import ir.ac.ut.iis.retrieval_tools.azimi.TaxonomyHierarchyReader;
import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.FeatureExtractor;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.LocalFeatureExtractor;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 *
 * @author shayan
 */
public class Calculator {

    public FeatureExtractor fe1;
    public FeatureExtractor fe2;
    SimilarityCalculator sc;

    public Calculator(FeatureExtractor fe1, FeatureExtractor fe2) {
        this.fe1 = fe1;
        this.fe2 = fe2;
    }

    public void setSc(SimilarityCalculator sc) {
        this.sc = sc;
    }

    public SimilarityCalculator getSc() {
        return sc;
    }

    public Calculator.MaxResult calculateSimilarity(String doc, Topic topic) {
        return sc.calculateSimilarity(doc, topic);
    }

    public static void main(String[] args) throws Exception {
//        String s1 = "Graph clustering has been an essential part in many methods and thus its accuracy has a significant effect on many applications. In addition, exponential growth of real-world graphs such as social networks, biological networks and electrical circuits demands clustering algorithms with nearly-linear time and space complexity. In this paper we propose Personalized PageRank Clustering (PPC) that employs the inherent cluster exploratory property of random walks to reveal the clusters of a given graph. We combine random walks and modularity to precisely and efficiently reveal the clusters of a graph. PPC is a top-down algorithm so it can reveal inherent clusters of a graph more accurately than other nearly-linear approaches that are mainly bottom-up. It also gives a hierarchy of clusters that is useful in many applications. PPC has a linear time and space complexity and has been superior to most of the available clustering algorithms on many datasets. Furthermore, its top-down approach makes it a flexible solution for clustering problems with different requirements.";
//        String s2 = "As a result of its increasing role in the enterprise, the Information Technology (IT) function is changing, morphing from a technology provider into a strategic partner. Key to this change is its ability to deliver business value by aligning and supporting the business objectives of the enterprise. IT Management frameworks such as ITIL (IT Infrastructure Library, [3]) provide best practices and processes that support the IT function in this transition. In this paper, we focus on one of the various cross-domain processes documented in ITIL involving the service level, incident, problem and change management processes and present a theoretical framework for the prioritization of service incidents based on their impact on the ability of IT to align with business objectives. We then describe the design of a prototype system that we have developed based on our theoretical framework and present how that solution for incident prioritization integrates with other IT management software products of the HP OpenviewTM management suite.";
//        String s1 = "Timed Rebeca is an extension of the actor-based modeling language Rebeca that supports timing features. Rebeca is purely actor-based with no shared variables and asynchronous message passing with no explicit receive. Both computation time and network delays can be modeled in Timed Rebeca. In this paper, we propose a new approach for checking schedulability and deadlock freedom of Timed Rebeca models. The key features of Timed Rebeca, asynchrony of actors and absence of shared variables, and the focus on events instead of states in the selected properties, led us to a significant reduction in the state space. In the proposed method, there is no unique time value for each state, instead of that we store the local time of each actor separately. We prove the bisimilarity of the generated transition system, called floating time transition system, and the state space generated from the original semantics of Timed Rebeca. In addition, to avoid state space explosion because of time progress, we define a type of equivalency among states called shift equivalency. The shift equivalence relation between states can be used for Timed Rebeca as the timing features are based on relative values. We developed a tool, and the experimental results show that our approach mitigates the state space explosion problem of the former methods and allows model checking of larger systems.";
        String s1 = "An email conversation thread is defined as a topic-centric discussion unit that is composed of exchanged emails among the same group of people by reply or forwarding. Detecting conversation threads contained in email corpora can be beneficial for both humans to digest the content of discussions and automatic methods to extract useful information from the conversations. This research explores two new feature-enriched learning approaches, LExLinC and LExTreC, to reconstruct linear structure and tree structure of conversation threads in email data. In this work, some simplifying assumptions considered in previous methods for extracting conversation threads are relaxed, which makes the proposed methods more powerful in detecting real conversations. Additionally, the supervised nature of the proposed methods makes them adaptable to new environments by automatically adjusting the features and their weights. Experimental results show that the proposed methods are highly effective in detecting conversation threads and outperform the existing methods.";
//        FeatureExtractor globalFeatureExtractor = new GlobalFeatureExtractor("index");
        FeatureExtractor localFeatureExtractor = new LocalFeatureExtractor("index/multi/1/", new TaxonomyHierarchyReader("/home/shayan/Desktop/Azimi/taxonomy2012.txt"));
        Calculator jsd = new Calculator(localFeatureExtractor, localFeatureExtractor);
        jsd.setSc(jsd.new KL_PerTopicSimilarityCalculator());
//        Double calcDivergence = jsd.calcDivergence(s1, s2);
//        System.out.println(calcDivergence);
        try (Classifier cl = new Classifier(new Topic.TopicParser(), jsd, s1, new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("js-results.txt"))))) {
            cl.crawl("/home/shayan/Desktop/Azimi/dataset-cleaned/3");
        }
    }

    public static Double klDivergence(Map<String, Double> doc1, Map<String, Double> doc2) throws Exception {
        double eps = 0.01;
        double r = 0;
        for (Map.Entry<String, Double> d1 : doc1.entrySet()) {
            Double d2 = doc2.get(d1.getKey());
            if (d2 == null) {
                d2 = 0.;
            }
            double inc = ((d1.getValue() + eps) * Math.log((d1.getValue() + eps) / (d2 + eps)));
//            System.out.println(d1.getKey() + " " + inc);
            r += inc;
        }
        return r;
    }

    public static List<String> tokenizeString(Analyzer analyzer, String string) {
        List<String> result = new ArrayList<>();
        try {
            try (TokenStream stream = analyzer.tokenStream(null, new StringReader(string))) {
                stream.reset();
                while (stream.incrementToken()) {
                    result.add(stream.getAttribute(CharTermAttribute.class).toString());
                }
            }
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
        return result;
    }

    public static class MaxResult {

        SampleDocument doc;
        double similarity;

        public MaxResult(SampleDocument doc, double similarity) {
            this.doc = doc;
            this.similarity = similarity;
        }

        public SampleDocument getDoc() {
            return doc;
        }

        public double getSimilarity() {
            return similarity;
        }

    }

    public interface SimilarityCalculator {

        Calculator.MaxResult calculateSimilarity(String doc, Topic topic);
    }

    public abstract class DivergenceCalculator {

        public Double calcDivergence(String s1, String s2) {
            List<String> s1Tokens;
            List<String> s2Tokens;
            try (Analyzer analyzer = new EnglishAnalyzer(CharArraySet.EMPTY_SET)) {
                s1Tokens = tokenizeString(analyzer, s1);
                s2Tokens = tokenizeString(analyzer, s2);
            }
            s1Tokens.removeAll(Config.stopWords);
            s2Tokens.removeAll(Config.stopWords);
            Double jsDivergence = divergence(fe1.getFeature(s1Tokens), fe2.getFeature(s2Tokens));
            return jsDivergence;
        }

        public abstract Double divergence(Map<String, Double> doc1, Map<String, Double> doc2);
    }
        public Double jsDivergence(Map<String, Double> doc1, Map<String, Double> doc2) {
            Set<String> keys = new TreeSet<>();
            keys.addAll(doc1.keySet());
            keys.addAll(doc2.keySet());
            Map<String, Double> cent = new HashMap<>();
            for (String key : keys) {
                Double v1 = doc1.get(key);
                if (v1 == null) {
                    v1 = 0.;
                }
                Double v2 = doc2.get(key);
                if (v2 == null) {
                    v2 = 0.;
                }
                cent.put(key, (v1 + v2) / 2);
            }

            Double kl1;
            Double kl2;
            try {
                kl1 = klDivergence(doc1, cent);
                kl2 = klDivergence(doc2, cent);
            } catch (Exception ex) {
                Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }
            return ((kl1 + kl2)) / 2;
        }

    public class JSDivergenceCalculator extends DivergenceCalculator {

        @Override
        public Double divergence(Map<String, Double> doc1, Map<String, Double> doc2) {
            return jsDivergence(doc1, doc2);
        }

    }

    public class KLDivergenceCalculator extends DivergenceCalculator {

        @Override
        public Double divergence(Map<String, Double> doc1, Map<String, Double> doc2) {
            Double kl;
            try {
                kl = klDivergence(doc1, doc2);
            } catch (Exception ex) {
                Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }
            return kl;
        }

    }

    public abstract class PerDocumentSimilarityCalculator implements SimilarityCalculator {

        public abstract Double calcDivergence(String s1, String s2);

        @Override
        public Calculator.MaxResult calculateSimilarity(String doc, Topic topic) {
            double maxSimilarity = Double.NEGATIVE_INFINITY;
            SampleDocument maxDocument = null;
            for (SampleDocument d : topic.getDocs()) {
                if (d.getText().trim().length() < 50) {
                    continue;
                }
                double sim;
                try {
                    sim = 1 - calcDivergence(doc, d.getText());
                } catch (Exception ex) {
                    Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
                if (maxSimilarity < sim) {
                    maxSimilarity = sim;
                    maxDocument = d;
                }
            }
//        System.out.println(maxDocument.getText());
            return new Calculator.MaxResult(maxDocument, maxSimilarity);
        }

    }

    public class FastPerTopicSimilarityCalculator_KL implements SimilarityCalculator {

        Map<String, Map<String, Double>> toBeClassified;
        final Map<String, Map<String, Double>> topics;

        public FastPerTopicSimilarityCalculator_KL(Map<String, Map<String, Double>> topics) {
            this.topics = topics;
        }

        public void setToBeClassified(Map<String, Map<String, Double>> toBeClassified) {
            this.toBeClassified = toBeClassified;
        }

        public Double calcDivergence(String s1, String s2) {
            return null;
        }

        @Override
        public Calculator.MaxResult calculateSimilarity(String doc, Topic topic) {
            Double kl;
            try {
                kl = klDivergence(toBeClassified.get(topic.getTopic()), topics.get(topic.getTopic()));
            } catch (Exception ex) {
                Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }

            return new Calculator.MaxResult(null, 1 - kl);
        }

    }
    public class FastPerTopicSimilarityCalculator_JS implements SimilarityCalculator {

        Map<String, Map<String, Double>> toBeClassified;
        final Map<String, Map<String, Double>> topics;

        public FastPerTopicSimilarityCalculator_JS(Map<String, Map<String, Double>> topics) {
            this.topics = topics;
        }

        public void setToBeClassified(Map<String, Map<String, Double>> toBeClassified) {
            this.toBeClassified = toBeClassified;
        }

        public Double calcDivergence(String s1, String s2) {
            return null;
        }

        @Override
        public Calculator.MaxResult calculateSimilarity(String doc, Topic topic) {
            Double js;
            try {
                js = jsDivergence(toBeClassified.get(topic.getTopic()), topics.get(topic.getTopic()));
            } catch (Exception ex) {
                Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }

            return new Calculator.MaxResult(null, 1 - js);
        }

    }

    public abstract class PerTopicSimilarityCalculator implements SimilarityCalculator {

        public abstract Double calcDivergence(String s1, String s2);

        @Override
        public Calculator.MaxResult calculateSimilarity(String doc, Topic topic) {
            double maxSimilarity = Double.NEGATIVE_INFINITY;
            SampleDocument maxDocument = null;
            StringBuilder sb = new StringBuilder();
            for (SampleDocument d : topic.getDocs()) {
                sb.append(d.getText()).append("\n");
            }
//        for (SampleDocument d : topic.getDocs()) {
//            if (d.getText().trim().length() < 50) {
//                continue;
//            }
//            double sim = 1 - calcDivergence(doc, d.getText());
            String toString = sb.toString();
            double sim;
            try {
                sim = 1 - calcDivergence(doc, toString);
            } catch (Exception ex) {
                Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }
            if (maxSimilarity < sim) {
                maxSimilarity = sim;
                maxDocument = new SampleDocument();
                maxDocument.setText(toString);
            }
//        }
//        System.out.println(maxDocument.getText());
            return new Calculator.MaxResult(maxDocument, maxSimilarity);
        }
    }

    public class JS_PerDocumentSimilarityCalculator extends PerDocumentSimilarityCalculator {

        DivergenceCalculator dc = new JSDivergenceCalculator();

        @Override
        public Double calcDivergence(String s1, String s2) {
            return dc.calcDivergence(s1, s2);
        }

    }

    public class KL_PerDocumentSimilarityCalculator extends PerDocumentSimilarityCalculator {

        DivergenceCalculator dc = new KLDivergenceCalculator();

        @Override
        public Double calcDivergence(String s1, String s2) {
            return dc.calcDivergence(s1, s2);
        }

    }

    public class KL_PerTopicSimilarityCalculator extends PerTopicSimilarityCalculator {

        DivergenceCalculator dc = new KLDivergenceCalculator();

        @Override
        public Double calcDivergence(String s1, String s2) {
            return dc.calcDivergence(s1, s2);
        }

    }

    public class JS_PerTopicSimilarityCalculator extends PerTopicSimilarityCalculator {

        DivergenceCalculator dc = new JSDivergenceCalculator();

        @Override
        public Double calcDivergence(String s1, String s2) {
            return dc.calcDivergence(s1, s2);
        }

    }

}