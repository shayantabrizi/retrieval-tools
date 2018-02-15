/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.azimi;

import ir.ac.ut.iis.retrieval_tools.citeseerx.PapersReader;
import ir.ac.ut.iis.retrieval_tools.domain.MyIterable;
import ir.ac.ut.iis.retrieval_tools.other.StopWordsExtractor;
import ir.ac.ut.iis.retrieval_tools.papers.BasePaper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shayan
 */
public class KeywordAnalyzer implements MyIterable<BasePaper> {

    static Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) {
        KeywordAnalyzer multiDisciplinaryAuthorExtractor = new KeywordAnalyzer();
        PapersReader papersReader = new PapersReader(multiDisciplinaryAuthorExtractor);
        papersReader.run("../citeseerx/papers_giant.txt");
        Map<String, Integer> sortByValue = StopWordsExtractor.MapUtil.sortByValue(map);
        for (Map.Entry<String, Integer> e : sortByValue.entrySet()) {
            if (e.getValue() > 50) {
                System.out.println(e.getKey() + " " + e.getValue());
            }
        }
        System.out.println(withKeywordCount);
    }

    private int count = 0;
    private static int withKeywordCount = 0;

    @Override
    public boolean doAction(BasePaper d) {
        boolean check = false;
        for (String s : d.getSubjects()) {
            String w = s.trim().toLowerCase();
            if (w.equals("keywords")
                    || w.equals("key words")
                    || w.equals("key-words")
                    || w.equals("1")
                    || w.equals("categories and subject descriptors")
                    || w.equals("2")
                    || w.equals("contents")
                    || w.startsWith("index terms")
                    || w.equals("d")
                    || w.equals("0")) {
                continue;
            }
            if (w.equals("i.3.7 [computer graphics")
                    || w.equals("i.3.5 [computer graphics")
                    || w.equals("i.3.3 [computer graphics")
                    || w.equals("i.3.1 [computer graphics")) {
                w = "computer graphics";
            }

            if (check == false) {
                withKeywordCount++;
                check = true;
            }

            Integer get = map.get(w);
            if (get == null) {
                get = 0;
            }
            get++;

            map.put(w, get);
        }
        count++;
        if (count % 10_000 == 0) {
            System.out.println(count);
        }
        return true;
    }

}
