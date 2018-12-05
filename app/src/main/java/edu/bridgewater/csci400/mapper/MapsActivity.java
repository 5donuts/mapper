package edu.bridgewater.csci400.mapper;

import android.graphics.Color;
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

import edu.bridgewater.csci400.mapper.util.Graph;

import static sun.text.bidi.BidiBase.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapsActivity mListener;
    private Graph g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        g = new Graph();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in and move the camera
        LatLng bridgewater = new LatLng(38.3789, -78.9694);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bridgewater));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.5f));


        //Polylines
        Polyline McKinneyToBowman = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.380709,-78.967992), new LatLng(38.379658,-78.970421))
                .width(5)
                .color(Color.GREEN));

        Polyline McKinneyToFlory = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.380709,-78.967992), new LatLng(38.378338,-78.971772))
                .width(5)
                .color(Color.GREEN));

        Polyline McKinneyToMemorial = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.380709,-78.967992), new LatLng(38.378195,-78.971542))
                .width(5)
                .color(Color.BLUE));

        Polyline NiningerToBowman = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(38.379658,-78.970421), new LatLng(38.377581,-78.970689))
                .width(5)
                .color(Color.BLUE));


        //Markers
        //Residence Halls
        Marker BlueRidge = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.379027,-78.969113))
                .title("BlueRidge")
                .snippet("ResidenceHall"));

        Marker WrightHall= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.379809,-78.970764))
                .title("Wright")
                .snippet("ResidenceHall"));

        Marker HerritageHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.380104,-78.970512))
                .title("Herritage")
                .snippet("ResidenceHall"));

        Marker Dillon= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378472,-78.967783))
                .title("Dillon")
                .snippet("ResidenceHall"));

        Marker Wakeman = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.379208,-78.966668))
                .title("Wakeman")
                .snippet("ResidenceHall"));

        Marker Daleville = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378809,-78.968368))
                .title("Daleville")
                .snippet("ResidenceHall"));

        Marker Geisert = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.380650,-78.969483))
                .title("Geisert")
                .snippet("ResidenceHall"));

        Marker StoneVillage = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.376340,-78.967922
                ))
                .title("StoneVillage")
                .snippet("ResidenceHall"));

        Marker Towers = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.379654,-78.966416))
                .title("Towers")
                .snippet("ResidenceHall"));


        //Academic Buildings
        Marker McKinney = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.380709,-78.967992))
                .title("McKinney")
                .snippet("AcademicBuilding"));

        Marker BowmanHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.379658,-78.970421))
                .title("Bowman")
                .snippet("AcademicBuilding"));

        Marker FloryHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378338,-78.971772))
                .title("Flory")
                .snippet("AcademicBuilding"));

        Marker MemorialHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378195,-78.971542))
                .title("Memorial")
                .snippet("AcademicBuilding"));

        Marker NiningerHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.377581,-78.970689))
                .title("Nininger")
                .snippet("AcademicBuilding"));

        Marker ColeHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378136,-78.969885))
                .title("Cole")
                .snippet("AcademicBuilding"));

        Marker MoomawHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378472,-78.968866))
                .title("Moomaw")
                .snippet("AcademicBuilding"));

        Marker RebeccaHall = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378720,-78.969044))
                .title("Rebecca")
                .snippet("AcademicBuilding"));

        Marker Carter= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.379019,-78.971402))
                .title("Carter")
                .snippet("AcademicBuilding"));

        //MISC
        Marker FunkHouser = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.377211,-78.966147
                ))
                .title("FunkHouser")
                .snippet("Gym"));

        Marker Library = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378825,-78.971016
                ))
                .title("Library")
                .snippet("Library"));

        Marker KCC = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.378539,-78.969767))
                .title("KCC")
                .snippet("DinningHall"));


    }
}


