package edu.bridgewater.csci400.mapper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.bridgewater.csci400.mapper.util.Destination;
import edu.bridgewater.csci400.mapper.util.Graph;
import edu.bridgewater.csci400.mapper.util.Node;

import com.google.gson.*;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowCloseListener, LocationListener {


    private GoogleMap mMap;
    private MapsActivity mListener;
    private LocationManager locationManager;
    private String networkLocation = LocationManager.NETWORK_PROVIDER;
    private String gpsLocation = LocationManager.GPS_PROVIDER;
    public static Graph GRAPH;
    public static final int PICK_DEST_REQUEST = 1;

    private static final Integer LOC = -1;
    private static final Integer NONE = 0;
    private static final Integer START = 1;
    private static final Integer DEST = 2;
    private Integer selection = NONE;
    private Marker startMarker = null;
    private Marker destMarker = null;
    private List<Marker> mapMarkers;
    private Marker myLoc = null;
    private boolean showLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GRAPH = new Graph(this);
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    public void viewList(View view) {
        Intent intent = new Intent(this, ListActivityExampleActivity.class);
        if (startMarker != null)
            intent.putExtra("startName", startMarker.getTitle());
        if (destMarker != null)
            intent.putExtra("destName", destMarker.getTitle());
        startActivityForResult(intent, PICK_DEST_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_DEST_REQUEST) {
            if (resultCode == RESULT_OK) {
                int start = data.getIntExtra("start", -1);
                int end = data.getIntExtra("end", -1);
                if (start >= 0 && end >= 0 && start != end) {
                    List<Destination> destinations = GRAPH.getDestinations();
                    Destination startDest = null;
                    Destination endDest = null;
                    for (int i = 0; i < destinations.size(); i++) {
                        if (destinations.get(i).getId() == start)
                            startDest = destinations.get(i);
                        if (destinations.get(i).getId() == end)
                            endDest = destinations.get(i);
                    }
                    if (startDest != null && endDest != null) {
                        //Adjust markers to match selected start and end points
                        if (startMarker != null) {
                            startMarker.setIcon(BitmapDescriptorFactory.fromAsset("marker.png"));
                            startMarker.setSnippet("Tap to start here");
                            startMarker.setTag(NONE);
                        }
                        if (destMarker != null) {
                            destMarker.setIcon(BitmapDescriptorFactory.fromAsset("marker.png"));
                            destMarker.setSnippet("Tap to start here");
                            destMarker.setTag(NONE);
                        }
                        selection = NONE;
                        for (int i = 0; i < mapMarkers.size(); i++) {
                            Marker marker = mapMarkers.get(i);
                            if (marker.getTitle().equals(startDest.getName())) {
                                marker.hideInfoWindow();
                                marker.setSnippet("Starting point");
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                marker.setTag(START);
                                startMarker = marker;
                                selection = START;
                            } else if (marker.getTitle().equals(endDest.getName())) {
                                marker.hideInfoWindow();
                                marker.setSnippet("Destination");
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                marker.setTag(DEST);
                                destMarker = marker;
                                marker.showInfoWindow();
                                //Center map on destination
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                            }
                        }

                        List<Polyline> path = GRAPH.getShortestPath(startDest, endDest);
                        // TODO complete shortest path computation, then add path to map
                    }
                }
            }
        }
    }

    /** Called when the user taps a marker */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.getTag() == null) {
            marker.setTag(NONE);
        }
        if (selection.equals(START) && marker.getTag() == NONE) {
            marker.setSnippet("Tap to navigate");
        }
        return false;
    }

    /** Called when the user taps a marker label */
    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
        if (marker.getTag().equals(LOC)) {
            List<Node> nodes = GRAPH.getNodes();
            Node closestNode = nodes.get(0);
            double minDistance = GRAPH.distance(closestNode.getPosition(), marker.getPosition());
            for (Node n : nodes) {
                double distance = GRAPH.distance(n.getPosition(), marker.getPosition());
                if (distance < minDistance) {
                    closestNode = n;
                    minDistance = distance;
                }
            }
            // TODO show as start marker, set closestNode as starting node for shortest path
        } else if (selection.equals(NONE) || selection.equals(DEST) && !(marker.equals(destMarker))) {
            marker.setSnippet("Starting point");
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            marker.setTag(START);
            if (startMarker != null) {
                startMarker.setIcon(BitmapDescriptorFactory.fromAsset("marker.png"));
                startMarker.setTag(NONE);
            }
            startMarker = marker;
            selection = START;
        } else if (!(marker.equals(startMarker) || marker.equals(destMarker))) {
            marker.setSnippet("Destination");
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            marker.setTag(DEST);
            if (destMarker != null) {
                destMarker.setIcon(BitmapDescriptorFactory.fromAsset("marker.png"));
                destMarker.setSnippet("Tap to start here");
                destMarker.setTag(NONE);
            }
            destMarker = marker;
            // TODO use startMarker and destMarker to compute and display path
        } else {
            if (marker.equals(startMarker) || selection.equals(DEST)) {
                marker.setSnippet("Tap to start here");
                if (selection.equals(DEST)) {
                    destMarker = null;
                    selection = NONE;
                } else {
                    startMarker = null;
                    selection = DEST;
                }
            } else {
                marker.setSnippet("Tap to navigate");
                destMarker = null;
            }
            marker.setTag(NONE);
            marker.setIcon(BitmapDescriptorFactory.fromAsset("marker.png"));
        }
        marker.showInfoWindow();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if (marker.getTag().equals(NONE)) {
            marker.setSnippet("Tap to start here");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference((float)15.5);

        List<Destination> destinations = GRAPH.getDestinations();
        mapMarkers = new ArrayList<>();
        for (Destination d: destinations) {
            mapMarkers.add(mMap.addMarker(new MarkerOptions()
                    .position(d.getDestPin())
                    .snippet("Tap to start here")
                    .title(d.getName())
                    .icon(BitmapDescriptorFactory.fromAsset("marker.png"))));
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);

        //Temporary demo code//
        /*BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("map_data.json"));
            reader = new BufferedReader(is);

            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            String json = out.toString();

            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            JsonArray nodes = jsonObject.getAsJsonObject("map_data").getAsJsonArray("nodes");
            double[] coords = new double[500];
            for (int i = 0; i < nodes.size(); i++) {
                Log.d("json", nodes.get(i).getAsJsonObject().get("latitude").getAsString());
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(nodes.get(i).getAsJsonObject().get("latitude").getAsDouble(), nodes.get(i).getAsJsonObject().get("longitude").getAsDouble()))
                        .radius(2)
                        .strokeWidth(7)
                        .strokeColor(0xFF49C7FF)
                        .fillColor(0xFFFFFFFF));
                coords[2*i] = nodes.get(i).getAsJsonObject().get("latitude").getAsDouble();
                coords[(2*i)+1] = nodes.get(i).getAsJsonObject().get("longitude").getAsDouble();
            }
            JsonArray edges = jsonObject.getAsJsonObject("map_data").getAsJsonArray("edges");
            for (int i = 0; i < edges.size(); i++) {
                Log.d("json", edges.get(i).getAsJsonObject().get("node1").getAsString());
                int node1 = edges.get(i).getAsJsonObject().get("node1").getAsInt();
                int node2 = edges.get(i).getAsJsonObject().get("node2").getAsInt();
                Polyline path = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(coords[(node1-1)*2], coords[(node1-1)*2+1]), new LatLng(coords[(node2-1)*2], coords[(node2-1)*2+1]))
                        .width(5)
                        .color(0xFF49C7FF));
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }*/
    }

    public void toggleLocation(View view) {
        Button locationButton = findViewById(R.id.button3);
        if (showLocation) {
            locationButton.setText(getString(R.string.locationShow));
            myLoc.remove();
            myLoc = null;
            locationManager.removeUpdates(this);
        } else {
            locationButton.setText(getString(R.string.locationHide));
            getLocation();
        }
        showLocation = !showLocation;
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(networkLocation);
        if (lastKnownLocation == null) {
            Log.d("location", "Location was null");
            lastKnownLocation = locationManager.getLastKnownLocation(gpsLocation);

        }
        if (lastKnownLocation != null) {
            Log.d("location", Double.toString(lastKnownLocation.getLatitude()));
            LatLng position = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            if (myLoc == null) {
                myLoc = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title("My Location")
                        .zIndex(2)
                        .icon(BitmapDescriptorFactory.fromAsset("locMarker.png")));
                myLoc.setTag(LOC);
            } else {
                myLoc.setPosition(position);
            }
        }
        locationManager.requestLocationUpdates(networkLocation, 0, 0, this);
    }

    public void onLocationChanged(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        if (myLoc == null) {
            myLoc = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("My Location")
                    .zIndex(2)
                    .icon(BitmapDescriptorFactory.fromAsset("locMarker.png")));
            myLoc.setTag(LOC);
        } else {
            myLoc.setPosition(position);
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}
}


