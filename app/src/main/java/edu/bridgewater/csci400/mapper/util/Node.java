package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.db.LocalDB;

public class Node {
    private int _id;
    private LatLng position;
    private int destId;

    public Node(int id, LatLng position, int destId) {
        _id = id;
        this.position = position;
        this.destId = destId;
    }

    public int getId() {
        return _id;
    }

    public LatLng getPosition() {
        return position;
    }

    public int getDestId() {
        return destId;
    }

    public List<Edge> getEdges() {
        return LocalDB.getEdgesWithNode(this);
    }

    public List<Node> getAdjacentNodes() {
        List<Edge> edges = getEdges();
        List<Node> nodes = new ArrayList<>();
        for(Edge e : edges)
            nodes.addAll(e.getNodes());
        return nodes;
    }

    public double distanceTo(Node n) {
        LatLng p1 = getPosition();
        LatLng p2 = n.getPosition();
        final double R =  6378137; // Earth’s mean radius in meters
        double dLat = deg2rad(p2.latitude - p1.latitude);
        double dLong = deg2rad(p2.longitude - p1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(p1.latitude)) * Math.cos(deg2rad(p2.latitude)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in meters
        return d;
    }

    private double deg2rad(double x) {
        return x * Math.PI / 180;
    }
}
