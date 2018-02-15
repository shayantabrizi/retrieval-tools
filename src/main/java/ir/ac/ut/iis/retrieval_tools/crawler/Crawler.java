/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.crawler;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Shayan
 * @param <D>
 */
public abstract class Crawler<D> implements IterableToFiles<D>, Closeable {    
    private final Parser parser;
    public Crawler(Parser parser) {
        this.parser = parser;
    }

    public void crawl(String sourceFolder) throws IOException {
        IterateOnFiles iof = new IterateOnFiles(this, parser);
        iof.iterate(new File(sourceFolder));
    }
}
