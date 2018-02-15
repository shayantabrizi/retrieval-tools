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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 *
 * @author Shayan
 */
public class TaxonomyIndexer extends Crawler<Topic> {

    Indexer indexer;

    public TaxonomyIndexer(Parser parser, Indexer indexer) throws IOException {
        super(parser);
        this.indexer = indexer;
    }

//    public static void convertTaxonomiyToARFF(String taxonomyFile, String arffFile) throws IOException {
//        List<SampleDocument> list = IterateOnFiles.parseFile(new File(taxonomyFile), new Topic().createDigester());
//        try (OutputStreamWriter os = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(arffFile)))) {
//            os.write("@RELATION 'Taxonomy'\n");
////            os.write("@ATTRIBUTE class {general and reference, HARDWARE}\n");
//            os.write("@ATTRIBUTE topic string\n");
//            os.write("@ATTRIBUTE text string\n");
//            os.write("@DATA\n");
//            for (SampleDocument s : list) {
//                os.write("'" + s.getTopic() + "','" + s.getText().replaceAll("\n", " ").replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\'") + "'\n");
//            }
//        }
//    }
    @Override
    public boolean doAction(File fileEntry, Topic d) {
        for (SampleDocument sd : d.getDocs()) {
//            try {
////                System.err.println(sd.getId());
//                osw.write(sd.getText() + "\n");
//            } catch (Exception ex) {
//                Logger.getLogger(TaxonomyReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
            Document doc = new Document();
            doc.add(new StringField("topic", d.getTopic(), Field.Store.YES));
            doc.add(new StringField("topicId", Integer.toString(d.getTopicId()), Field.Store.YES));
            doc.add(new StringField("id", Integer.toString(sd.getId()), Field.Store.YES));
            doc.add(new StringField("localId", Integer.toString(sd.getLocalId()), Field.Store.YES));
            doc.add(new StringField("rank", Integer.toString(sd.getRank()), Field.Store.YES));

//            FieldType type = new FieldType();
//            type.setStored(true);
//            type.setStoreTermVectors(true);

            doc.add(new TextField("text", sd.getText(), Field.Store.YES));

            indexer.addDocument(doc);

        }
        return true;
    }

    @Override
    public void close() throws IOException {
        indexer.close();
    }
}
