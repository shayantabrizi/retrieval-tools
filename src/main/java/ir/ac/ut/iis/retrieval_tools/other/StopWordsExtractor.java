package ir.ac.ut.iis.retrieval_tools.other;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author Shayan
 */
public class StopWordsExtractor {

    public static void main(String[] args) throws IOException {

        Directory index = FSDirectory.open(new File("indexer").toPath());

        IndexReader reader = DirectoryReader.open(index);
        Terms terms = MultiFields.getTerms(reader, "text");
        TermsEnum iterator = terms.iterator();
        BytesRef bytesRef;
        Map<String, Integer> map = new HashMap<>();
        while ((bytesRef = iterator.next()) != null) {
            String s = bytesRef.utf8ToString();
            int docFreq = iterator.docFreq();
            long totalTermFreq = iterator.totalTermFreq();
            map.put(s, docFreq);
//            System.out.println(s + " " + docFreq + " " + totalTermFreq);
        }
        Map<String, Integer> sortByValue = MapUtil.sortByValue(map);
        int i = 0;
        for (Map.Entry<String, Integer> e : sortByValue.entrySet()) {
            i++;
            System.out.println(i + " " + e.getKey() + ": " + e.getValue());
            if (i == 3_000) {
                break;
            }
        }

    }

    public static class MapUtil {

        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
            List<Map.Entry<K, V>> list
                    = new LinkedList<>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                @Override
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return -(o1.getValue()).compareTo(o2.getValue());
                }
            });

            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }

        private MapUtil() {
        }
    }
}
