/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.domain;

/**
 *
 * @author shayan
 * @param <N>
 */
public class Edge<N extends Node> {

    private final N src;
    private final N dst;
    private float[] weight = new float[1];

    public Edge(N src, N dst) {
        this.src = src;
        this.dst = dst;
        for (int i = 0; i < weight.length; i++) {
            this.weight[i] = 1;
        }
    }

    public Edge(N src, N dst, float[] weight) {
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }

    public float[] getWeight() {
        return weight;
    }

    public N getSrc() {
        return src;
    }

    public N getDst() {
        return dst;
    }

    public N getOtherSide(N n) {
        if (src.equals(n)) {
            return dst;
        } else {
            return src;
        }
    }

}
