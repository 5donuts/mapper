package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

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

    public double distanceTo(Node n) {
        LatLng pos1 = position;
        LatLng pos2 = n.getPosition();

        return Math.sqrt(Math.pow(pos2.latitude - pos1.latitude, 2)
                + Math.pow(pos2.longitude - pos1.longitude, 2));
    }
}
