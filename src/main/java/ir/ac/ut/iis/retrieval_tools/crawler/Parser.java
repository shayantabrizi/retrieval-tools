/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.crawler;


/**
 *
 * @author shayan
 * @param <T>
 */
public interface Parser<T> {

    T parse(String content);

}
