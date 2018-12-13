package edu.bridgewater.csci400.mapper.dijkstra;

import edu.bridgewater.csci400.mapper.db.LocalDB;

public class Edge {
    private final int id;
    private final Vertex source, destination;
    private final double weight;

    public Edge(int id, Vertex source, Vertex destination, double weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public edu.bridgewater.csci400.mapper.util.Edge toUtilEdge() {
        return LocalDB.getEdge(id);
    }

    @Override
    public String toString() {
        return source + " " + destination + " " + weight;
    }

    @Override
    public boolean equals(Object o) {
        Edge e = (Edge) o;
        return this.id == e.getId() && this.source.equals(e.getSource()) && this.destination.equals(e.getDestination()) &&
                this.weight == e.getWeight();
    }
}
