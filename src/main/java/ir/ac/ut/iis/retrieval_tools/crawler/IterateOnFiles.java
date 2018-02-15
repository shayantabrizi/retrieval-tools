/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.crawler;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shayan
 */
public class IterateOnFiles {

    private final static String xml10pattern = "[^"
            + "\u0009\r\n"
            + "\u0020-\uD7FF"
            + "\uE000-\uFFFD"
            + "\ud800\udc00-\udbff\udfff"
            + "]";
    IterableToFiles it;
    private final Parser parser;

    public IterateOnFiles(IterableToFiles it, Parser parser) {
        this.it = it;
        this.parser = parser;
    }

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

    public boolean iterate(final File folder) {
        File[] listFiles = folder.listFiles();
        Arrays.sort(listFiles);
        for (final File fileEntry : listFiles) {
            if (fileEntry.isDirectory()) {
                boolean iterate = iterate(fileEntry);
                if (iterate == false) {
                    return false;
                }
            } else {
                Object doc = parseFile(fileEntry, parser);
                boolean doAction = it.doAction(fileEntry, doc);
                if (doAction == false) {
                    return true;
                }
            }
        }
        return true;
    }

    public static <T> T parseFile(final File fileEntry, Parser<T> parser) {
        try {
            String fileContent = readFile(fileEntry.getAbsolutePath(), Charset.forName("UTF-8"));
            T doc;

            doc = parser.parse(fileContent.replaceAll(xml10pattern, " "));
            return doc;

        } catch (IOException ex) {
            Logger.getLogger(IterateOnFiles.class.getName()).log(Level.SEVERE, fileEntry.getName(), ex);
            throw new RuntimeException();
        }
    }
}
