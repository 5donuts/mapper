package edu.bridgewater.csci400.mapper.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// see http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
public class DijkstraSP {
    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Double> distance;

    public DijkstraSP(Graph graph) {
        // create a copy of the data structure so we can manipulate it
        this.nodes = new ArrayList<>(graph.getVertices());
        this.edges = new ArrayList<>(graph.getEdges());
    }

    public void execute(Vertex source) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();

        distance.put(source, 0.0);
        unSettledNodes.add(source);

        while(unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for(Vertex target : adjacentNodes) {
            if(getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    private double getDistance(Vertex node, Vertex target) {
        for(Edge e : edges) {
            if(e.getSource().equals(node) && e.getDestination().equals(target))
                return e.getWeight();
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<>();
        for(Edge e : edges) {
            if(e.getSource().equals(node) && !isSettled(e.getDestination()))
                neighbors.add(e.getDestination());
        }
        return neighbors;
    }

    private Vertex getMinimum(Set<Vertex> vertices) {
        Vertex minimum = null;
        for(Vertex v : vertices) {
            if(minimum == null) {
                minimum = v;
            }
            else {
                if(getShortestDistance(v) < getShortestDistance(minimum))
                    minimum = v;
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex v) {
        return settledNodes.contains(v);
    }

    private double getShortestDistance(Vertex destination) {
        Double d = distance.get(destination);
        if (d == null)
            return Double.POSITIVE_INFINITY;
        return d;
    }

    // Returns the path from the source to the selected target and NULL if no such path exists
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex step = target;
        // check if a path exists
        if(predecessors.get(step) == null)
            return null;
        path.add(step);
        while(predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // put it into the correct order
        Collections.reverse(path);
        return path;
    }
}
