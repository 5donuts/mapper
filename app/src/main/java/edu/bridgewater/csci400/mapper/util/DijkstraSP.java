package edu.bridgewater.csci400.mapper.util;

import java.util.List;
import java.util.PriorityQueue;

public class DijkstraSP {
    private final List<Node> nodes;
    private final List<Edge> edges;

    private final Node source;

    private double dist[], prev[];
    private PriorityQueue<Double> pq;

    public DijkstraSP(List<Node> nodes,List<Edge> edges, Node source) {
        this.nodes = nodes;
        this.edges = edges;
        this.source = source;


    }

    public List<Edge> pathTo(Node target) {
        return null;
    }
}
