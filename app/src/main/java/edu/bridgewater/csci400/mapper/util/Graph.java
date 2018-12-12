package edu.bridgewater.csci400.mapper.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.db.LocalDB;

public class Graph {
    private static final float POLYLINE_WIDTH = 5.0F;
    private static final int POLYLINE_COLOR = Color.BLUE;
    private static final int SELECTED_POLYLINE_COLOR = Color.RED;
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Destination> destinations;

    public Graph(Context context) {
        LocalDB.openDB(context);
        nodes= LocalDB.getNodes();
        edges = LocalDB.getEdges();
        destinations = LocalDB.getDestinations();
    }

    public List<Polyline> getPaths(GoogleMap map){
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

    public List<LatLng> getDestinationPins() {
        List<LatLng> pinList = new ArrayList<>();
        for(Destination d : destinations) {
            pinList.add(d.getDestPin());
        }
        return pinList;
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

    public List<Polyline> getShortestPath(Node start, Node dest) {
        return null;
    }

    public List<Polyline> getShortestPath(Node start, Destination dest) {
        // choose node to use as destination
        List<Node> destNodes = dest.getNodes();
        Node dNode = destNodes.get(0);
        for (int i = 1; i < destNodes.size(); i++) {
            if (start.distanceTo(destNodes.get(i)) < start.distanceTo(dNode))
                dNode = destNodes.get(i);
        }

        return getShortestPath(start, dNode);
    }

    public List<Polyline> getShortestPath(Destination start, Destination dest) {
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

        return getShortestPath(sNode, dNode);
    }

    public double distance(LatLng p1, LatLng p2) {
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
}
