package edu.bridgewater.csci400.mapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.util.Destination;

public class ListActivity extends Activity {

    private Spinner start;
    private Spinner dest;
    private List<Destination> destinations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity layout xml file.
        setContentView(R.layout.list_activity);

        Intent intent = getIntent();

        // Create a list data which will be displayed in inner ListView.
        destinations = MapsActivity.GRAPH.getDestinations();
        List<String> listData = new ArrayList<>();
        for(Destination d : destinations)
            listData.add(d.getName());

        // Create the ArrayAdapter using the spinner layout and the list data.
        start = findViewById(R.id.spinner);
        dest = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        start.setAdapter(adapter);
        dest.setAdapter(adapter);

        // Get selected destinations from maps activity intent
        String startName = intent.getStringExtra("startName");
        String destName = intent.getStringExtra("destName");
        if (startName != null || destName != null) {
            for (int i = 0; i < destinations.size(); i++) {
                Destination destination = destinations.get(i);
                if (destination.getName().equals(startName)) {
                    start.setSelection(i);
                } else if (destination.getName().equals(destName)) {
                    dest.setSelection(i);
                }
            }
        }
    }

    public void navigate(View view) {
        int startPoint = destinations.get(start.getSelectedItemPosition()).getId();
        int endPoint = destinations.get(dest.getSelectedItemPosition()).getId();

        //Create intent to return selections to map activity
        Intent mapIntent = new Intent();
        mapIntent.putExtra("start", startPoint);
        mapIntent.putExtra("end", endPoint);
        setResult(Activity.RESULT_OK, mapIntent);
        finish();
    }
}