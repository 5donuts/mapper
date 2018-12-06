package edu.bridgewater.csci400.mapper;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.util.Destination;

public class ListActivityExampleActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity layout xml file.


        setContentView(R.layout.list_activity);


        // Create a list data which will be displayed in inner ListView.
        List<Destination> destinations = MapsActivity.GRAPH.getDestinations();
        List<String> listData = new ArrayList<>();
        for(Destination d : destinations)
            listData.add(d.getName());

        // Create the ArrayAdapter use the item row layout and the list data.
        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this, R.layout.activity_row_list, listData);

        // Set this adapter to inner ListView object.
        this.setListAdapter(listDataAdapter);
    }
}