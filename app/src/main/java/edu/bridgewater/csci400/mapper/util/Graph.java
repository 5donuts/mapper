package edu.bridgewater.csci400.mapper.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

import edu.bridgewater.csci400.mapper.db.LocalDB;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Destination> destinations;

    public Graph() {
        nodes= LocalDB.getNodes();
        // TODO implement getEdges()
        //TODO implement getDestinations()
    }

    public List<Polyline> getPaths(){
        return null;
    }

    public List<LatLng> getDestinationPins() {
        return null;
    }

    public List<Polyline> getShortestPath(Node start, Destination dest) {
        return null;
    }


}


