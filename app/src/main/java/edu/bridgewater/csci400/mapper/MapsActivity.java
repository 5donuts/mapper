package edu.bridgewater.csci400.mapper;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    }
}

//Juan's Code on MapsActivity 
package com.ju4nc4rl0sjun10rgmail.bridgewatercampusmap;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapsActivity mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // BC MAP
        LatLng bridgewater = new LatLng(38.3789, -78.9694);
        mMap.addMarker(new MarkerOptions().position(bridgewater).title("Bridgewater"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bridgewater));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        //Polyline
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.3789, -78.9684), new LatLng(38.379566,-78.970181))
                .width(5)
                .color(Color.RED));

        //Marker 
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.3789, -78.9684))
                .title("College")
                .snippet("BCMAPPER"));

    }
}
