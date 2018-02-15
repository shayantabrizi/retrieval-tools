/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.informal_scorer;

import ir.ac.ut.iis.retrieval_tools.informal_scorer.domain.IDocument;
import ir.ac.ut.iis.retrieval_tools.informal_scorer.domain.ITerm;
import ir.ac.ut.iis.retrieval_tools.informal_scorer.domain.SimpleTerm;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shayan
 */
public abstract class InformalScorer {

    List<ITerm> informalWords;

    public InformalScorer(List<ITerm> informalWords) {
        this.informalWords = informalWords;
    }

    public InformalScorer(String fileName) {
        informalWords = new LinkedList<>();
        try (Scanner informals = new Scanner(new FileInputStream(fileName), "UTF-8")) {
            while (informals.hasNextLine()) {
                String s = informals.nextLine();
                informalWords.add(new SimpleTerm(s).normalize());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InformalScorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract double score(IDocument doc);
}
