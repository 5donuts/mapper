package edu.bridgewater.csci400.mapper.dijkstra;

import edu.bridgewater.csci400.mapper.db.LocalDB;
import edu.bridgewater.csci400.mapper.util.Node;

public class Vertex {
    private final int id;

    public Vertex(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Node toUtilNode() {
        return LocalDB.getNode(id);
    }

    @Override
    public String toString() {
        return "nodeId: " + id;
    }

    @Override
    public boolean equals(Object v) {
        return this.id == ((Vertex) v).id;
    }
}
