/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.crawler;

import java.io.File;

/**
 *
 * @author Shayan
 * @param <D>
 */
public interface IterableToFiles<D> {

    boolean doAction(final File fileEntry, final D d);
}
