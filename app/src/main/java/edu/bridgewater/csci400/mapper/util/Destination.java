package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Destination {
    private int _id;
    private String name;
    private LatLng destPin;

    public Destination(int id, String name, LatLng destPin) {
        this._id = id;
        this.name = name;
        this.destPin = destPin;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public LatLng getDestPin() {
        return destPin;
    }
}
