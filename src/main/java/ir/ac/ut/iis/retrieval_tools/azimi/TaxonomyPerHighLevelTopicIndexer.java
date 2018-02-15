/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.azimi;

import ir.ac.ut.iis.retrieval_tools.crawler.Crawler;
import ir.ac.ut.iis.retrieval_tools.crawler.Parser;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.similarities.BM25Similarity;

/**
 *
 * @author Shayan
 */
public class TaxonomyPerHighLevelTopicIndexer extends Crawler<Topic> {

    Map<String, Indexer> indexers = new TreeMap<>();
    final private TaxonomyHierarchyReader thr;
    String rootFolder;

    public TaxonomyPerHighLevelTopicIndexer(Parser parser, String rootFolder, TaxonomyHierarchyReader thr) throws IOException {
        super(parser);
        this.rootFolder = rootFolder;
        this.thr = thr;
    }

    @Override
    public boolean doAction(File fileEntry, Topic d) {
        System.out.println(d.getTopic());
        for (SampleDocument sd : d.getDocs()) {
            Document doc = new Document();
            doc.add(new StringField("topic", d.getTopic(), Field.Store.YES));
            doc.add(new StringField("topicId", Integer.toString(d.getTopicId()), Field.Store.YES));
            doc.add(new StringField("id", Integer.toString(sd.getId()), Field.Store.YES));
            doc.add(new StringField("localId", Integer.toString(sd.getLocalId()), Field.Store.YES));
            doc.add(new StringField("rank", Integer.toString(sd.getRank()), Field.Store.YES));

//            FieldType type = new FieldType();
//            type.setStored(true);
//            type.setStoreTermVectors(true);

            doc.add(new TextField("text", sd.getText(), Store.YES));
            Set<String> ancestors = thr.getByTopicName(d.getTopic()).getAncestorsAsString(1);
            for (String anc : ancestors) {
                Indexer g = indexers.get(anc);
                if (g == null) {
                    try {
                        g = new Indexer(rootFolder + anc, new BM25Similarity(), true);
                    } catch (IOException ex) {
                        Logger.getLogger(TaxonomyPerHighLevelTopicIndexer.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException();
                    }
                    indexers.put(anc, g);
                }
                g.addDocument(doc);
            }
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        for (Indexer i : indexers.values()) {
            i.close();
        }
    }
}
