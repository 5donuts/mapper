package edu.bridgewater.csci400.mapper.util;

import java.util.List;

public class Destination {
    private int _id;
    private String name;
    private List<Node> nodes;

    public Destination(int id, String name, List<Node> nodes) {
        this._id = id;
        this.name = name;
        this.nodes = nodes;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
