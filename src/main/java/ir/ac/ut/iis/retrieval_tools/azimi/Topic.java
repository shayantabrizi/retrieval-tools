
package ir.ac.ut.iis.retrieval_tools.azimi;

import ir.ac.ut.iis.retrieval_tools.crawler.Parser;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

/**
 *
 * @author shayan
 */
public class Topic implements Serializable {

    private String topic;
    private int topicId;
    private final List<SampleDocument> docs = new LinkedList<>();

    public Topic() {
    }

    public Topic(String topic, int topicId) {
        this.topic = topic;
        this.topicId = topicId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public List<SampleDocument> getDocs() {
        return Collections.unmodifiableList(docs);
    }

    public void addDoc(SampleDocument doc) {
        this.docs.add(doc);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (SampleDocument d : docs) {
            s.append("[");
            s.append(d.toString());
            s.append("], ");
        }
        return "Topic{" + "topic=" + topic + ", topicId=" + topicId + ", docs=" + s + '}';
    }

    public static class TopicParser implements Parser<Topic> {

        private final Digester digester;

        public TopicParser() {
            digester = new Digester();
            digester.addObjectCreate("PAGES", Topic.class);
            digester.addBeanPropertySetter("PAGES/QUERYNO", "topicId");
            digester.addBeanPropertySetter("PAGES/QUERY", "topic");
            digester.addObjectCreate("PAGES/DOC", SampleDocument.class);
            digester.addSetNext("PAGES/DOC", "addDoc");
            digester.addBeanPropertySetter("PAGES/DOC/DOCNO", "id");
            digester.addBeanPropertySetter("PAGES/DOC/LOCALID", "localId");
            digester.addBeanPropertySetter("PAGES/DOC/TEXT", "text");
            digester.addBeanPropertySetter("PAGES/DOC/RANK", "rank");
        }

        @Override
        public Topic parse(String content) {
            Topic parse;
            try {
                parse = digester.parse(content);
            } catch (IOException | SAXException ex) {
                Logger.getLogger(Topic.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }
            return parse;
        }

    }

}
