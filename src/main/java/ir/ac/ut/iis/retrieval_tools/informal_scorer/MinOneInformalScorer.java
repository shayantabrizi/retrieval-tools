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
public class MinOneInformalScorer extends InformalScorer {

    public MinOneInformalScorer(String fileName) {
        super(fileName);
    }

    public MinOneInformalScorer(List<ITerm> informalWords) {
        super(informalWords);
    }

    @Override
    public double score(IDocument doc) {
        for (ITerm t : informalWords) {
            if (doc.termFreq(t) > 0) {
                return 1;
            }
        }
        return 0;
    }

}
