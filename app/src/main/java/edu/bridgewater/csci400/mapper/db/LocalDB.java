package edu.bridgewater.csci400.mapper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
        //get Edge
        String query = Edges_T.GET_EDGE;
        String[] data = {id + ""};
        Cursor c = db.rawQuery(query, data);
        if(c == null || c.getCount() == 0)
                return null;
        c.moveToFirst();

        int node_1 = c.getInt(c.getColumnIndex(Edges_T.NODE_1));
        int node_2 = c.getInt(c.getColumnIndex(Edges_T.NODE_2));

        Node n1 = getNode(node_1);
        Node n2 = getNode(node_2);

        Edge e = new Edge(id, n1, n2);

        return e;
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
        // TODO figure out boolean
        boolean vis = true;

        return new Node(id, new LatLng(lat, lon), vis);
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
