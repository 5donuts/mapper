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

public class ListActivityExampleActivity extends Activity {

    private Spinner start;
    private Spinner dest;

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

        // Create the ArrayAdapter using the spinner layout and the list data.
        start = (Spinner) findViewById(R.id.spinner);
        dest = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        start.setAdapter(adapter);
        dest.setAdapter(adapter);
    }

    public void navigate(View view) {
        String startPoint = (String) start.getSelectedItem();
        String endPoint = (String) dest.getSelectedItem();

        //Create intent to return selections to map activity
        Intent mapIntent = new Intent();
        mapIntent.putExtra("start", startPoint);
        mapIntent.putExtra("end", endPoint);
        setResult(Activity.RESULT_OK, mapIntent);
        finish();
    }
}