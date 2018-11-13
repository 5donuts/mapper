package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.db.LocalDB;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Destination> destinations;

    public Graph() {
        nodes= LocalDB.getNodes();
        edges = LocalDB.getEdges();
        destinations = LocalDB.getDestinations();
    }

    public List<Polyline> getPaths(){
        return null;
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


