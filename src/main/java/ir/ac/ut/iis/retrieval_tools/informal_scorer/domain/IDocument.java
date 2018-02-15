/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.informal_scorer.domain;

import java.io.Serializable;

/**
 *
 * @author Shayan
 */
public interface IDocument extends Serializable {

    int termFreq(ITerm t);

    ITerm[] terms();

    int termCount();

}
