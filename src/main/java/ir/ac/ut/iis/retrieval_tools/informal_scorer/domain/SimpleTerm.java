/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.informal_scorer.domain;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shayan
 */
public class SimpleTerm implements ITerm {

    private final String term;

    public SimpleTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return term;
    }

    public SimpleTerm normalize() {
        try {
            return this.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(SimpleTerm.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class not clonable");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleTerm other = (SimpleTerm) obj;
        return Objects.equals(this.term, other.term);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.term);
        return hash;
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    protected SimpleTerm clone() throws CloneNotSupportedException {
        return new SimpleTerm(term);
    }

}
