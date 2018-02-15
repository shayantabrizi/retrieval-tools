/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.ac.ut.iis.retrieval_tools.domain;

/**
 *
 * @author shayan
 */
public interface MyIterable<D> {

    boolean doAction(final D d);
}
