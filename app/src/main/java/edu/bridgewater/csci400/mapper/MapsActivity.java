package edu.bridgewater.csci400.mapper;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.*;
import android.util.Log;
import java.io.*;
import com.google.android.gms.maps.model.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Bridgewater College.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Bridgewater College and move the camera
        LatLng bridgewaterCollege = new LatLng(38.3787678, -78.9705121);
        mMap.addMarker(new MarkerOptions().position(bridgewaterCollege).title("Bridgewater College"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bridgewaterCollege, 20)); // zoom level of 20

        //Temporary demo code//
        BufferedReader reader = null;
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
        }
        //--------------------//
    }
}
