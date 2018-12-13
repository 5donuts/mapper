package edu.bridgewater.csci400.mapper.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.bridgewater.csci400.mapper.db.LocalDB;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

public class Graph {
    private static final float POLYLINE_WIDTH = 5.0F;
    private static final int POLYLINE_COLOR = Color.BLUE;

    private List<Node> nodes;
    private List<Edge> edges;
    private List<Destination> destinations;

    private final EdgeWeightedDigraph g;
    private DijkstraSP shortestPaths;
    private Node prevSource;

    private Map<DirectedEdge, Edge> edgeMap; // used to create polylines after calculating shortest paths

    public Graph(Context context) {
        // get information from database
        LocalDB.openDB(context);
        nodes = LocalDB.getNodes();
        edges = LocalDB.getEdges();
        destinations = LocalDB.getDestinations();

        // build algs4 objects for Dijkstra's shortest path algorithm
        // the graph is initialized to hold more than the number of vertices and edges to handle cases where
        // the id of the node or edge exceeds the capacity of the data structure. (e.g., an ID of 229 on a range of 0-228)
        edgeMap = new HashMap<>();
        g = new EdgeWeightedDigraph(nodes.size() + (int) (nodes.size() * 0.5), edges.size() + (int) (edges.size() * 0.5));
        for(Edge e : edges) {
            List<Node> edgeNodes = e.getNodes();
            DirectedEdge de = new DirectedEdge(edgeNodes.get(0).getId(), edgeNodes.get(1).getId(),
                    e.distanceBetweenNodes());
            g.addEdge(de);
            edgeMap.put(de, e);
            // database edges are undirected, so a second parallel edge must be added
            de = new DirectedEdge(edgeNodes.get(1).getId(), edgeNodes.get(0).getId(),
                    e.distanceBetweenNodes());
            g.addEdge(de);
            edgeMap.put(de, e);
        }
    }

    public List<Polyline> getAllPaths(GoogleMap map){
        return buildPolylines(edges, map);
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public double getDistanceBetween(LatLng p1, LatLng p2) {
        double R =  6378137; // Earth’s mean radius in meters
        double dLat = radians(p2.latitude - p1.latitude);
        double dLong = radians(p2.longitude - p1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radians(p1.latitude)) * Math.cos(radians(p2.latitude)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in meters
        return d;
    }

    private double radians(double x) {
        return x * Math.PI / 180;
    }

    private List<Polyline> buildPolylines(List<Edge> edges, GoogleMap map) {
        List<Polyline> polylines = new ArrayList<>();
        for(Edge e : edges) {
            List<Node> nodes = e.getNodes();
            Polyline p = map.addPolyline(new PolylineOptions()
                    .add(nodes.get(0).getPosition(), nodes.get(1).getPosition())
                    .width(POLYLINE_WIDTH)
                    .color(POLYLINE_COLOR)
            );
            polylines.add(p);
        }
        return polylines;
    }

    public List<Polyline> getShortestPath(Node start, Node dest, GoogleMap map) {
        if(start == null || dest == null || map == null)
            throw new NullPointerException();

        if(shortestPaths == null || prevSource != start) {
            prevSource = start;
            shortestPaths = new DijkstraSP(g, start.getId());
        }

        // build the mapper Edge objects from the algs4 DirectedEdge objects
        Iterable<DirectedEdge> spEdges = shortestPaths.pathTo(dest.getId());
        List<Edge> edges = new ArrayList<>();
        for(DirectedEdge de : spEdges) {
            Edge e = edgeMap.get(de);
            // TODO figure out why e is sometimes null
            edges.add(e);
        }

        // build the polylines
        return buildPolylines(edges, map);
    }

    public List<Polyline> getShortestPath(Node start, Destination dest, GoogleMap map) {
        // choose node to use as destination
        List<Node> destNodes = dest.getNodes();
        Node dNode = destNodes.get(0);
        for (int i = 1; i < destNodes.size(); i++) {
            if (start.distanceTo(destNodes.get(i)) < start.distanceTo(dNode))
                dNode = destNodes.get(i);
        }

        return getShortestPath(start, dNode, map);
    }

    public List<Polyline> getShortestPath(Destination start, Destination dest, GoogleMap map) {
        // choose starting node
        List<Node> startNodes = start.getNodes();
        Node sNode = startNodes.get(0);
        Node destPos = new Node(-1, dest.getDestPin(), -1); // dummy node for distanceTo calls
        for(int i = 1; i < startNodes.size(); i++) {
            if(startNodes.get(i).distanceTo(destPos) < sNode.distanceTo(destPos))
                sNode = startNodes.get(i);
        }

        // choose ending node
        List<Node> destNodes = dest.getNodes();
        Node dNode = destNodes.get(0);
        for (int i = 1; i < destNodes.size(); i++) {
            if (sNode.distanceTo(destNodes.get(i)) < sNode.distanceTo(dNode))
                dNode = destNodes.get(i);
        }

        return getShortestPath(sNode, dNode, map);
    }
}
