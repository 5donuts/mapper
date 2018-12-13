package edu.bridgewater.csci400.mapper.dijkstra;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.util.Node;

public class Graph {
    private List<Vertex> vertices;
    private List<Edge> edges;

    public Graph(List<Node> nodes, List<edu.bridgewater.csci400.mapper.util.Edge> edges) {
        // convert Node and util.Edge objects to internal Vertex and Edge types
        vertices = new ArrayList<>();
        for(Node n : nodes) {
            Vertex v = new Vertex(n.getId());
            vertices.add(v);
        }
        this.edges = new ArrayList<>();
        for(edu.bridgewater.csci400.mapper.util.Edge e : edges) {
            List<Node> edgeNodes = e.getNodes();
            Vertex v1 = new Vertex(edgeNodes.get(0).getId());
            if(vertices.contains(v1)) // list.contains(Object) uses Object.equals(Object) for comparison
                v1 = vertices.get(vertices.indexOf(v1)); // list.indexOf(Object) works the same way
            Vertex v2 = new Vertex(edgeNodes.get(1).getId());
            if(vertices.contains(v2))
                v2 = vertices.get(vertices.indexOf(v2));
            this.edges.add(new Edge(e.getId(), v1, v2, e.getWeight()));
        }
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
