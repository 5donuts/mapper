package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Destination {
    private int _id;
    private String name;
    private LatLng destPin;
    private List<Node> nodes;

    public Destination(int id, String name, List<Node> nodes, LatLng destPin) {
        this._id = id;
        this.name = name;
        this.nodes = nodes == null ? new ArrayList<Node>() : nodes;
        this.destPin = destPin;
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

    public LatLng getDestPin() {
        return destPin;
    }

    public void addNode(Node n) {
        nodes.add(n);
    }
}
