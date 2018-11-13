package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

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
}
