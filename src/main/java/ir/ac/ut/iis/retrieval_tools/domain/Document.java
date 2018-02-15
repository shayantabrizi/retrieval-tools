/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.domain;

import java.io.Serializable;

/**
 *
 * @author shayan
 */
public abstract class Document implements Comparable<Document>, Serializable {

    private final int id;

    public Document(int id) {
        this.id = id;
    }

    public abstract String getText();

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Document other = (Document) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Document o) {
        return Integer.compare(o.getId(), getId());
    }

}
