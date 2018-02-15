/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.crawler;

/**
 *
 * @author shayan
 */
public class StringParser implements Parser<String> {

    @Override
    public String parse(String content) {
        return content;
    }

}
