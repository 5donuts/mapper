package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

public class Node {
    private int _id;
    private LatLng position;
    private boolean visible;

    public Node(int id, LatLng position, boolean visible) {
        _id = id;
        this.position = position;
        this.visible = visible;
    }

    public int getId() {
        return _id;
    }

    public LatLng getPosition() {
        return position;
    }

    public boolean isVisible() {
        return visible;
    }
}
