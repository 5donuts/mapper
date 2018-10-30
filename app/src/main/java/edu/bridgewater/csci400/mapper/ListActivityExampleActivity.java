package edu.bridgewater.csci400.mapper;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivityExampleActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity layout xml file.
        setContentView(layout.activity_list_activity_example);

        // Create a list data which will be displayed in inner ListView.
        List<String> listData = new ArrayList<String>();
        listData.add("Yount Hall");
        listData.add("Memorial Hall");
        listData.add("Flory Hall");
        listData.add("Carter Center");
        listData.add("John Kenny Forrer Library");
        listData.add("Wright Hall");
        listData.add("Heritage Hall");
        listData.add("Geisert Hall");
        listData.add("Bowman Hall");
        listData.add("McKinney Center");
        listData.add("Wampler Towers");
        listData.add("Wakeman Hall");
        listData.add("Blue Ridge Hall");
        listData.add("Daleville Hall");
        listData.add("Dillon Hall");
        listData.add("Funkhouser Center");
        listData.add("Moomaw Hall");
        listData.add("Rebecca Hall");
        listData.add("Kline Campus Center");
        listData.add("Cole Hall");
        listData.add("Stone Village");
        listData.add("Bicknell House");
        listData.add("Nininger Hall");

        // Create the ArrayAdapter use the item row layout and the list data.
        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_activity_example_row, R.id.listRowTextView, listData);

        // Set this adapter to inner ListView object.
        this.setListAdapter(listDataAdapter);
    }

    // When user click list item, this method will be invoked.
    @Override
    protected void onListItemClick(ListView listView, View v, int position, long id) {
        // Get the list data adapter.
        ListAdapter listAdapter = listView.getAdapter();
        // Get user selected item object.
        Object selectItemObj = listAdapter.getItem(position);
        String itemText = (String)selectItemObj;

        // Create an AlertDialog to show.
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(itemText);
        alertDialog.show();
    }
}