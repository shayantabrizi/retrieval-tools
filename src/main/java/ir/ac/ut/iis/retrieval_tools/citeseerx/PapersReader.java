/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.citeseerx;

import ir.ac.ut.iis.retrieval_tools.azimi.TaxonomyHierarchyReader;
import ir.ac.ut.iis.retrieval_tools.azimi.Topic;
import ir.ac.ut.iis.retrieval_tools.azimi.Topic.TopicParser;
import ir.ac.ut.iis.retrieval_tools.classifier.Calculator;
import ir.ac.ut.iis.retrieval_tools.classifier.Calculator.FastPerTopicSimilarityCalculator_KL;
import ir.ac.ut.iis.retrieval_tools.classifier.Calculator.JS_PerTopicSimilarityCalculator;
import ir.ac.ut.iis.retrieval_tools.classifier.Classifier;
import ir.ac.ut.iis.retrieval_tools.classifier.FastPaperClassifier;
import ir.ac.ut.iis.retrieval_tools.classifier.LMExtractor;
import ir.ac.ut.iis.retrieval_tools.classifier.TopicClassifier;
import ir.ac.ut.iis.retrieval_tools.crawler.Crawler;
import ir.ac.ut.iis.retrieval_tools.domain.MyIterable;
import ir.ac.ut.iis.retrieval_tools.feature_extractor.LocalFeatureExtractor;
import ir.ac.ut.iis.retrieval_tools.papers.Author;
import ir.ac.ut.iis.retrieval_tools.papers.BasePaper;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shayan
 */
public class PapersReader {

    final MyIterable<BasePaper> it;

    public PapersReader(MyIterable<BasePaper> it) {
        this.it = it;
    }

    public void run(String papersFile) {
        int i = 0;
        SimpleDateFormat sdfmt1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try (Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(papersFile)))) {
            sc.useDelimiter("\n");
            boolean check = false;
            while (sc.hasNext()) {
                i++;
                String nextLine = sc.next();
                if (check) {
                    System.out.println(nextLine);
                }
                Integer id = Integer.parseInt(nextLine);
                int isMerged = sc.nextInt();
                String docId = sc.next();
                String identifier = sc.next();
                String source = sc.next();
                String uri = sc.next();
                String lang = sc.next();
                String title = sc.next();
                String abs = sc.next();
                final String next = sc.next();
                Date date;
                try {
                    date = sdfmt1.parse(next);
                } catch (ParseException ex) {
                    Logger.getLogger(PapersReader.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
                String subjects = sc.next();
                sc.next();
                String refs = sc.next();
                String authors = sc.next();
                if (abs.length() + title.length() < 150) {
                    throw new RuntimeException();
//                    continue;
                }
//                if (id.equals(835386l)) {
//                    check = true;
////                    continue;
//                }
//                if (check == false) {
//                    continue;
//                }
                BasePaper p = new BasePaper(id, docId, date, title, abs, identifier, source, lang);
                p.setIsMerged((isMerged == 1));
                if (!subjects.isEmpty()) {
                    for (String subject : subjects.split(",")) {
                        if (!subject.isEmpty()) {
                            p.addSubject(subject);
                        }
                    }
                }

                for (String ref : refs.split(",")) {
                    if (!ref.isEmpty()) {
                        p.addUnprocessableRef(ref);
                    }
                }
                for (String author : authors.split(",")) {
                    if (!author.isEmpty()) {
                        p.addAuthor(new Author(Integer.parseInt(author), null));
                    }
                }
                if (it.doAction(p) == false) {
                    break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PapersReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
//        papersClassifier();
//        topicsClassifier();
        multiThreadedTopicsClassifier();

    }

//    private static void slowClassifier() throws FileNotFoundException {
//        Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("result.txt")));
//        LocalFeatureExtractor localFeatureExtractor = new LocalFeatureExtractor("index/multi/1/", new TaxonomyHierarchyReader("/home/shayan/Desktop/Azimi/taxonomy2012.txt"));
//        Calculator jsd = new Calculator(localFeatureExtractor, localFeatureExtractor);
//        jsd.setSc(jsd.new KL_PerTopicSimilarityCalculator());
//        try (PaperClassifier paperClassifier = new PaperClassifier(localFeatureExtractor, jsd, writer)) {
//            PapersReader papersReader = new PapersReader(paperClassifier);
//            papersReader.run("papers.txt");
//        }
//    }
    private static void papersClassifier() throws IOException, FileNotFoundException {
        //        slowClassifier();
        try (Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("result.txt")))) {
            TaxonomyHierarchyReader thr = new TaxonomyHierarchyReader(Classifier.azimiDatasetHome + "taxonomy2012.txt");
            LocalFeatureExtractor localFeatureExtractor = new LocalFeatureExtractor("index/multi/1/", thr);
            Calculator jsd = new Calculator(localFeatureExtractor, localFeatureExtractor);
            LMExtractor lme = new LMExtractor(new Topic.TopicParser(), localFeatureExtractor);
            lme.crawl("/home/shayan/Desktop/Azimi/dataset-cleaned/3");
            jsd.setSc(jsd.new FastPerTopicSimilarityCalculator_KL(lme.getMap()));
            try (FastPaperClassifier paperClassifier = new FastPaperClassifier(lme, jsd, writer)) {
                PapersReader papersReader = new PapersReader(paperClassifier);
                papersReader.run("papers.txt");
            }
        }
    }

    private static void multiThreadedTopicsClassifier() {
        TaxonomyHierarchyReader thr = new TaxonomyHierarchyReader(Classifier.azimiDatasetHome + "taxonomy2012.txt");
        List<Thread> list = new LinkedList<>();
        List<Runnable> runs = new LinkedList<>();
        makeThread(1, thr, runs, list, 0, 30);
        makeThread(2, thr, runs, list, 30, 70);
        makeThread(3, thr, runs, list, 70, 120);
        makeThread(4, thr, runs, list, 120, 180);
        makeThread(5, thr, runs, list, 180, 250);
        makeThread(6, thr, runs, list, 250, 330);
        makeThread(7, thr, runs, list, 330, 420);
        makeThread(8, thr, runs, list, 420, 524);
        for (int i = 0; i < runs.size(); i++) {
            try {
                list.get(i).join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PapersReader.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }
            try {
                ((AutoCloseable) runs.get(i)).close();
            } catch (Exception ex) {
                Logger.getLogger(PapersReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void makeThread(int i, TaxonomyHierarchyReader thr, List<Runnable> runs, List<Thread> list, int minCount, int maxCount) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("topics-classes-" + i + ".txt")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PapersReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Runnable myRunnable = new MyRunnable(writer, thr, minCount, maxCount);
        runs.add(myRunnable);
        Thread thread = new Thread(myRunnable);
        thread.start();
        System.out.println("Thread " + i + " started.");
        list.add(thread);
    }

    private static void topicsClassifier(Writer writer, TaxonomyHierarchyReader thr, int minCount, int maxCount) {
        LocalFeatureExtractor localFeatureExtractor = new LocalFeatureExtractor("index/multi/1/", thr);
        Calculator jsd = new Calculator(localFeatureExtractor, localFeatureExtractor);
        jsd.setSc(jsd.new JS_PerTopicSimilarityCalculator());
        try (TopicClassifier topicClassifier = new TopicClassifier(localFeatureExtractor, jsd, writer)) {
            topicClassifier.setMinCount(minCount);
            topicClassifier.setMaxCount(maxCount);
            TopicsCrawler topicsReader = new TopicsCrawler(new Topic.TopicParser(), topicClassifier);
            topicsReader.crawl("/shayan/dataset-cleaned/3");
        } catch (IOException ex) {
            Logger.getLogger(PapersReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

    private static class TopicsCrawler extends Crawler<Topic> {

        MyIterable<Topic> it;

        TopicsCrawler(TopicParser parser, MyIterable<Topic> it) {
            super(parser);
            this.it = it;
        }

        @Override
        public boolean doAction(File fileEntry, Topic t) {
            return it.doAction(t);
        }

        @Override
        public void close() {
        }
    }

    public static class MyRunnable implements Runnable, Closeable {

        Writer writer;
        TaxonomyHierarchyReader thr;
        int minCount;
        int maxCount;

        public MyRunnable(Writer writer, TaxonomyHierarchyReader thr, int minCount, int maxCount) {
            this.writer = writer;
            this.thr = thr;
            this.minCount = minCount;
            this.maxCount = maxCount;
        }

        @Override
        public void run() {
            topicsClassifier(writer, thr, minCount, maxCount);
        }

        @Override
        public void close() throws IOException {
            writer.close();
        }

    }

}
