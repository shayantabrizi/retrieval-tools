/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.informal_scorer;

import ir.ac.ut.iis.retrieval_tools.informal_scorer.domain.IDocument;
import ir.ac.ut.iis.retrieval_tools.informal_scorer.domain.ITerm;
import java.util.List;

/**
 *
 * @author Shayan
 */
public class LinearInformalScorer extends InformalScorer {

    public LinearInformalScorer(String fileName) {
        super(fileName);
    }

    public LinearInformalScorer(List<ITerm> informalWords) {
        super(informalWords);
    }

    @Override
    public double score(IDocument doc) {
        int count = 0;
        for (ITerm t : informalWords) {
            count += doc.termFreq(t);
        }
        return ((double) count) / doc.termCount();
    }

}
