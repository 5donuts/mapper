package edu.bridgewater.csci400.mapper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.bridgewater.csci400.mapper.util.Destination;
import edu.bridgewater.csci400.mapper.util.Edge;
import edu.bridgewater.csci400.mapper.util.Node;

public class LocalDB {
    private LocalDB() {} // make class uninstantiable

    // Error logging tag
    private static final String TAG = "LocalDB";

    // Database information
    private static final String DATABASE_NAME = "Mapper_DB";
    private static final int DATABASE_VERSION = 1;

    // Database objects
    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;

    // return codes
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;

    /**
     * Connect to the database
     * @param context
     */
    public static void openDB(Context context) {
        if (db == null) {
            dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Disconnect from the database
     */
    public static void closeDB() {
        db = null;
        dbHelper.close();
    }

    /**
     * Get an edge from the database
     * @param id the id of the database record to fetch
     * @return an {@code Edge} object representing the database record
     */
    public static Edge getEdge(int id) {
        if(db == null) {
            Log.e(TAG, "DB must be opened be getEdge(int) can execute.");
            return null;
        }

        // get the Edge record
        String query = Edges_T.GET_EDGE;
        String[] data = {id + ""};
        Cursor c = db.rawQuery(query, data);
        if(c == null || c.getCount() == 0)
                return null;
        c.moveToFirst();

        // get the corresponding Node records
        int nodeId1 = c.getInt(c.getColumnIndex(Edges_T.NODE_1));
        int nodeId2 = c.getInt(c.getColumnIndex(Edges_T.NODE_2));
        Node node1 = getNode(nodeId1);
        Node node2 = getNode(nodeId2);

        c.close();

        // build the Edge object
        return new Edge(id, node1, node2);
    }

    /**
     * Get a node from the database
     * @param id the id of the database record to fetch
     * @return a {@code Node} object representing the database record
     */
    public static Node getNode(int id) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNode(int) can execute.");
            return null;
        }

        // get the Node record
        String query = Nodes_T.GET_NODE;
        String[] data = {id + ""};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        c.moveToFirst();

        double lat = c.getDouble(c.getColumnIndex(Nodes_T.LATITUDE));
        double lon = c.getDouble(c.getColumnIndex(Nodes_T.LONGITUDE));
        boolean vis = c.getInt(c.getColumnIndex(Nodes_T.VISIBLE)) != 0; // stored as INT in database

        c.close();

        // build the Node object
        return new Node(id, new LatLng(lat, lon), vis);
    }

    /**
     * Get all visible nodes from the database
     * @return a {@code List} of {@code Node} objects representing the database records
     */
    public static List<Node> getVisibleNodes() {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getVisibleNodes() can execute.");
            return null;
        }

        // get all visible Node records
        String query = Nodes_T.GET_VISIBLE_NODES;
        String[] data = {};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        List<Node> nodes = new ArrayList<>();
        while(c.moveToNext()) {
            // build the Node object
            int id = c.getInt(c.getColumnIndex(Nodes_T._ID));
            double lat = c.getDouble(c.getColumnIndex(Nodes_T.LATITUDE));
            double lon = c.getDouble(c.getColumnIndex(Nodes_T.LONGITUDE));
            boolean vis = c.getInt(c.getColumnIndex(Nodes_T.VISIBLE)) != 0;
            nodes.add(new Node(id, new LatLng(lat, lon), vis)); // add it to the list
        }

        c.close();

        return nodes;
    }

    /**
     * Helper class that sets up the database/upgrades it
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            db.execSQL(Nodes_T.CREATE_TABLE);
            db.execSQL(Edges_T.CREATE_TABLE);
            db.execSQL(Destinations_T.CREATE_TABLE);
            db.execSQL(Destination_Nodes_T.CREATE_TABLE);

            // TODO add the routine for getting Daniel/Josh's JSON data into the DB here
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application database from version " + oldVersion + " to " +
                    newVersion + ". This will destroy all old data.");

            db.execSQL(Destination_Nodes_T.DELETE_TABLE);
            db.execSQL(Destinations_T.DELETE_TABLE);
            db.execSQL(Edges_T.DELETE_TABLE);
            db.execSQL(Nodes_T.DELETE_TABLE);

            db.execSQL(Nodes_T.CREATE_TABLE);
            db.execSQL(Edges_T.CREATE_TABLE);
            db.execSQL(Destinations_T.CREATE_TABLE);
            db.execSQL(Destination_Nodes_T.CREATE_TABLE);

            // TODO add the JSON routine here too
        }
    }
}
