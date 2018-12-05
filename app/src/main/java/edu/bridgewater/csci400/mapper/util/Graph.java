package edu.bridgewater.csci400.mapper.util;

import android.graphics.Color;

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

    public Graph() {
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

    public List<Polyline> getShortestPath(Node start, Destination dest) {
        return null;
    }


}


