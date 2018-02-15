/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.iis.retrieval_tools.azimi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shayan
 */
public class TaxonomyHierarchyReader {

    Map<String, Node> map = new TreeMap<>();

    public TaxonomyHierarchyReader(String taxonomyFileAddr) {
        Set<String> set = new TreeSet<>();
        Stack<Node> st = new Stack<>();
        try (Scanner sc = new Scanner(new FileInputStream(taxonomyFileAddr))) {
            int nodeNumber = 0;
            st.add(new Node(nodeNumber++, null, 0));
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                if (s.isEmpty()) {
                    while (st.peek().getNodeId() != 0) {
                        st.pop();
                    }
                    break;
                }
                int level;
                if (s.startsWith("\t\t\t\t\t")) {
                    level = 6;
                } else if (s.startsWith("\t\t\t\t")) {
                    level = 5;
                } else if (s.startsWith("\t\t\t")) {
                    level = 4;
                } else if (s.startsWith("\t\t")) {
                    level = 3;
                } else if (s.startsWith("\t")) {
                    level = 2;
                } else {
                    level = 1;
                }
                if (level > 3) {
                    continue;
                }
                String name = s.trim();
                Node get = map.get(name);
                if (get != null) {
//                    throw new RuntimeException("Redundant Names!");
                    if (get.getLevel() != level) {
                        set.add(name);
                    }
                } else {
                    Node node = new Node(nodeNumber++, s.trim(), level);
                    map.put(node.getNodeName(), node);
                    get = node;
                }
                while (st.peek().getLevel() > level) {
                    st.pop();
                }
                if (st.peek().getLevel() < level) {
                    st.peek().addChild(get);
                    get.addParent(st.peek());
                    st.push(get);
                } else if (st.peek().getLevel() == level) {
                    st.pop();
                    st.peek().addChild(get);
                    get.addParent(st.peek());
                    st.push(get);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaxonomyHierarchyReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
//        showTaxonomy(st.peek());
        if (!set.isEmpty()) {
            for (String s : set) {
                System.out.println(s);
            }
            throw new RuntimeException();
        }

    }

    public Node getByTopicName(String name) {
        return map.get(name);
    }

    public Set<Node> getTopics(int level) {
        Set<Node> set = new TreeSet<>();
        for (Node v : map.values()) {
            if (v.getLevel() == level) {
                set.add(v);
            }
        }
        return set;
    }

    private static void showTaxonomy(Node node) {
        for (int i = 0; i < node.level; i++) {
            System.out.print("\t");
        }
        System.out.println(node.getNodeName());
        for (Node n : node.getChildren()) {
            showTaxonomy(n);
        }
    }

    public static class Node implements Comparable<Node> {

        private final int nodeId;
        private final String nodeName;
        private final int level;
        private final List<Node> children = new LinkedList<>();
        private final List<Node> parents = new LinkedList<>();

        public Node(int nodeId, String nodeName, int level) {
            this.nodeId = nodeId;
            this.nodeName = nodeName;
            this.level = level;
        }

        public int getNodeId() {
            return nodeId;
        }

        public String getNodeName() {
            return nodeName;
        }

        public int getLevel() {
            return level;
        }

        public List<Node> getChildren() {
            return Collections.unmodifiableList(children);
        }

        public void addChild(Node node) {
            this.children.add(node);
        }

        public List<Node> getParents() {
            return Collections.unmodifiableList(parents);
        }

        public void addParent(Node node) {
            this.parents.add(node);
        }

        public Set<Node> getAncestors(int level) {
            return getAncestors(level, this);
        }

        public Set<String> getAncestorsAsString(int level) {
            Set<Node> ancestors = getAncestors(level, this);
            Set<String> set = new TreeSet<>();
            for (Node a: ancestors) {
                set.add(a.getNodeName());
            }
            return set;
        }

        private Set<Node> getAncestors(int level, Node n) {
            Set<Node> list = new TreeSet<>();
            if (n.getLevel() == level) {
                list.add(n);
                return list;
            }
            for (Node node : n.getParents()) {
                list.addAll(getAncestors(level, node));
            }
            return list;
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
            if (!Objects.equals(this.nodeName, other.nodeName)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Node o) {
            return nodeName.compareTo(o.getNodeName());
        }

    }

//    public static void main(String[] args) throws FileNotFoundException {
//        readTaxonomy(Result.parentDir + "taxonomy2012.txt");
//
//    }
}
