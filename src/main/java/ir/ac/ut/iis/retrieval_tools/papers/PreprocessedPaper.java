/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.papers;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shayan
 */
public class PreprocessedPaper implements Serializable {

    private String docId;
    private final List<String> dates = new LinkedList<>();
    private String title;
    private final List<String> creators = new LinkedList<>();
    private String abs;
    private final List<String> subjects = new LinkedList<>();
    private String identifier;
    private String source;
    private String lang;
    private final List<String> relations = new LinkedList<>();

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCreators() {
        return Collections.unmodifiableList(creators);
    }

    public void addCreator(String creator) {
        this.creators.add(creator);
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public List<String> getDates() {
        return Collections.unmodifiableList(dates);
    }

    public void addDate(String date) {
        dates.add(date);
    }

    public List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getRelations() {
        return Collections.unmodifiableList(relations);
    }

    public void addRelation(String relation) {
        relations.add(relation);
    }

    @Override
    public String toString() {
        return docId + " " + dates.toString() + " " + identifier + " " + source + " " + lang + " " + relations.toString();
    }

}
