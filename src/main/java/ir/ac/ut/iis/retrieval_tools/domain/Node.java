/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.domain;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author shayan
 * @param <N>
 * @param <E>
 * @param <Base>
 */
public abstract class Node<N extends Node, E extends Edge<N>, Base extends Comparable<Base>> implements Comparable<Node<N, E, Base>> {

    private final Base id;

    public abstract void addEdge(E e);

    public abstract List<E> getEdges();

    public Node(Base id) {
        this.id = id;
    }

    public Base getId() {
        return id;
    }

    @Override
    public int compareTo(Node<N, E, Base> o) {
        return o.getId().compareTo(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
