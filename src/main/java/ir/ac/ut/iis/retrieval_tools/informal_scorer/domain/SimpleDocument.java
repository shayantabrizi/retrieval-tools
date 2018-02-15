/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.informal_scorer.domain;

/**
 *
 * @author Shayan
 */
public class SimpleDocument implements IDocument {

    private final String doc;

    public SimpleDocument(String doc) {
        this.doc = doc;
    }

    @Override
    public int termFreq(ITerm t) {
        int count = 0;
        for (ITerm term : terms()) {
            if (t.equals(term)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public ITerm[] terms() {
        String[] split = doc.split(" ");
        ITerm[] terms = new ITerm[split.length];
        for (int i = 0; i < split.length; i++) {
            terms[i] = new SimpleTerm(split[i]);
        }
        return terms;
    }

    @Override
    public String toString() {
        return doc;
    }

    @Override
    public int termCount() {
        return terms().length;
    }

}
