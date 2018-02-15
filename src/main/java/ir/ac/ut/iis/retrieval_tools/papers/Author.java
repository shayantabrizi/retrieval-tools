/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.papers;

import ir.ac.ut.iis.retrieval_tools.domain.Edge;
import ir.ac.ut.iis.retrieval_tools.domain.Node;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shayan
 */
public class Author extends Node<Author, Edge<Author>, Integer> {

    private final String name;
    private final List<Edge<Author>> coAuthors = new LinkedList<>();

    public Author(int id, String name) {
        super(id);
        this.name = name;
    }


    @Override
    public void addEdge(Edge<Author> e) {
        coAuthors.add(e);
    }

    @Override
    public List<Edge<Author>> getEdges() {
        return Collections.unmodifiableList(coAuthors);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Author{" + "name=" + name + '}';
    }

}
