/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.azimi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Shayan
 */
public class Indexer {

    IndexWriter writer;

    public Indexer(String indexFolder, Similarity similarity, boolean create) throws IOException {
        Directory dir = FSDirectory.open(new File(indexFolder).toPath());
        Analyzer analyzer = new EnglishAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        iwc.setSimilarity(similarity);

        if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }
        iwc.setRAMBufferSizeMB(700.0);
        writer = new IndexWriter(dir, iwc);
    }

    public void addDocument(Document doc) {
        if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
            try {
                // New index, so we just add the document (no old document can be there):
                writer.addDocument(doc);
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Existing index (an old copy of this document may have been indexed) so 
            // we use updateDocument instead to replace the old one matching the exact 
            // path, if present:
//            System.out.println("updating " + file);
//            writer.updateDocument(new Term("path", file.getPath()), doc);
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
