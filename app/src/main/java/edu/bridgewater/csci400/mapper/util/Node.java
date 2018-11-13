package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

public class Node {
    private int _id;
    private LatLng position;

    public Node(int id, LatLng position) {
        _id = id;
        this.position = position;
    }

    public int getId() {
        return _id;
    }

    public LatLng getPosition() {
        return position;
    }
}
