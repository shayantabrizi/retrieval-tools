/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.papers;

import ir.ac.ut.iis.retrieval_tools.domain.Document;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author shayan
 */
public class BasePaper extends Document {

    private final String docId;
    private Date date;
    private String title;
    private List<Author> authors = new LinkedList<>();
    private String abs;
    private List<String> subjects = new LinkedList<>();
    private boolean isMerged = false;
    private final String identifier;
    private final String source;
    private final String lang;
    private final List<BasePaper> refs = new LinkedList<>();
    private final List<String> unprocessableRefs = new LinkedList<>();

    public BasePaper(int id, String docId, Date date, String title, String abs, String identifier, String source, String lang) {
        super(id);
        this.docId = docId;
        this.date = date;
        this.title = title;
        this.abs = abs;
        this.identifier = identifier;
        this.source = source;
        this.lang = lang;
    }

    public BasePaper(PreprocessedPaper p, int id) throws ParseException {
        super(id);
        this.docId = p.getDocId().replaceAll("oai:CiteSeerXPSU:", "");
        Date d = null;
        for (String s : p.getDates()) {
            if (s.length() < 5) {
                d = new SimpleDateFormat("yyyy").parse(s);
            }
        }
        this.date = d;
        this.title = p.getTitle();
        this.abs = p.getAbs();
        this.identifier = p.getIdentifier();
        this.source = p.getSource();
        this.lang = p.getLang();
        this.subjects.addAll(p.getSubjects());
    }

    @Override
    public String getText() {
        return abs;
    }

    public String getDocId() {
        return docId;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(Author a) {
        authors.add(a);
    }

    public String getAbs() {
        return abs;
    }

    public List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSource() {
        return source;
    }

    public String getLang() {
        return lang;
    }

    public List<BasePaper> getRefs() {
        return Collections.unmodifiableList(refs);
    }

    public void addRef(BasePaper p) {
        refs.add(p);
    }

    public List<String> getUnprocessableRefs() {
        return Collections.unmodifiableList(unprocessableRefs);
    }

    public void addUnprocessableRef(String p) {
        unprocessableRefs.add(p);
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    @Override
    public String toString() {
        return "Paper{" + "id=" + getId() + ", docId=" + docId + ", date=" + date + ", title=" + title + ", authors=" + authors + ", abs=" + abs + ", subjects=" + subjects + ", identifier=" + identifier + ", source=" + source + ", lang=" + lang + '}';
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIsMerged() {
        return isMerged;
    }

    public void setIsMerged(boolean isMerged) {
        this.isMerged = isMerged;
    }

    public String getURI() {
        return source;
    }

}
