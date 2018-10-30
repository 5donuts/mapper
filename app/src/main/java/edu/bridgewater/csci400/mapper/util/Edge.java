package edu.bridgewater.csci400.mapper.util;

import java.util.ArrayList;
import java.util.List;

public class Edge {
    private int _id;
    private Node node_1;
    private Node node_2;

    public Edge(int id, Node node_1, Node node_2) {
        _id = id;
        this.node_1 = node_1;
        this.node_2 = node_2;
    }

    public int getId() {
        return _id;
    }

    public List<Node> getNodes() {
        List<Node> l = new ArrayList<>();
        l.add(node_1);
        l.add(node_2);
        return l;
    }
}
