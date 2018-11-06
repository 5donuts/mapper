package edu.bridgewater.csci400.mapper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

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
     * Add an edge to the database
     * @param e the {@code Edge} object representing the record to add. Must not be {@code null}.
     * @return a code representing {@link #SUCCESS} or {@link #FAILURE}
     */
    public static int addEdge(Edge e) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before addEdge(Edge) can execute.");
            return FAILURE;
        }
        if (e == null) {
            Log.e(TAG, "Cannot add a null Edge record to the database.");
            return FAILURE;
        }

        List<Node> nodes = e.getNodes();
        ContentValues values = new ContentValues(3);
        values.put(Edges_T._ID, e.getId());
        values.put(Edges_T.NODE_1, nodes.get(0).getId());
        values.put(Edges_T.NODE_2, nodes.get(1).getId());

        long results = db.insert(Edges_T.TABLE_NAME, null, values);
        return results == -1 ? FAILURE : SUCCESS;
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

    public static List<Edge> getEdges() {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNodes() can execute.");
            return null;
        }

        // get all visible Node records
        String query = Edges_T.GET_EDGES;
        String[] data = {};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        List<Edge> edges = new ArrayList<>();
        while(c.moveToNext()) {
            // build the Node object
            int id = c.getInt(c.getColumnIndex(Edges_T._ID));
            int node_1 = c.getInt(c.getColumnIndex(Edges_T.NODE_1));
            Node n1 = getNode(node_1);
            int node_2 = c.getInt(c.getColumnIndex(Edges_T.NODE_2));
            Node n2 = getNode(node_2);
            edges.add(new Edge(id, n1 , n2)); // add it to the list
        }

        c.close();

        return edges;
    }

    public static Edge getEdgeWithNode(Node n) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getEdgeWithNode(Node) can execute.");
            return null;
        }
        String query = Edges_T.GET_EDGE_WITH_NODE;
        String[] data = {n.getId() + "", n.getId() + ""};
        Cursor c = db.rawQuery(query, data);
        if(c == null || c.getCount() == 0)
            return null;
        c.moveToFirst();

        // get the corresponding Node records
        int id = c.getInt(c.getColumnIndex(Edges_T._ID));
        int nodeId1 = c.getInt(c.getColumnIndex(Edges_T.NODE_1));
        int nodeId2 = c.getInt(c.getColumnIndex(Edges_T.NODE_2));
        Node node1 = getNode(nodeId1);
        Node node2 = getNode(nodeId2);

        c.close();

        return new Edge(id, node1, node2);
    }

    /**
     * Add a node to the database
     * @param n the {@code Node} object representing the record to add. Must not be {@code null}.
     * @return a code representing {@link #SUCCESS} or {@link #FAILURE}
     */
    public static int addNode(Node n) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before addNode(Node) can execute.");
            return FAILURE;
        }
        if (n == null) {
            Log.e(TAG, "Cannot add a null Node record to the database.");
            return FAILURE;
        }

        ContentValues values = new ContentValues(4);
        values.put(Nodes_T._ID, n.getId());
        values.put(Nodes_T.LATITUDE, n.getPosition().latitude);
        values.put(Nodes_T.LONGITUDE, n.getPosition().longitude);
        values.put(Nodes_T.VISIBLE, n.isVisible());

        long results = db.insert(Nodes_T.TABLE_NAME, null, values);
        return results == -1 ? FAILURE : SUCCESS;
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
    public static List<Node> getNodes() {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNodes() can execute.");
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
     * Add a destination record to the database
     * @param d the {@code Destination} object representing the record in the database.
     *          Must not be {@code null}.
     * @return a code representing {@link #SUCCESS} or {@link #FAILURE}.
     */
    public static int addDestination(Destination d) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before addDestination(Destination) can execute.");
            return FAILURE;
        }
        if (d == null) {
            Log.e(TAG, "Cannot add a null Destination record to the database.");
            return FAILURE;
        }

        ContentValues values = new ContentValues(2);
        values.put(Destinations_T._ID, d.getId());
        values.put(Destinations_T.NAME, d.getName());

        long results = db.insert(Destinations_T.TABLE_NAME, null, values);
        return results == -1 ? FAILURE : SUCCESS;
    }

    /**
     * Add a record to the destination nodes table of the database
     * @param d the {@code Destination} object representing the destination associated with the
     *          given nodes. Must not be {@code null}.
     * @return a code representing {@link #SUCCESS} or {@link #FAILURE}.
     */
    public static int addDestinationNodes(Destination d) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before addDestinationNodes(Destination) can execute.");
            return FAILURE;
        }
        if (d == null) {
            Log.e(TAG, "Cannot add a null Destination Nodes record to the database.");
            return FAILURE;
        }

        List<Node> nodes = d.getNodes();
        for (int i = 0, len = nodes.size(); i < len; i++) {
            ContentValues values = new ContentValues(2);
            values.put(Destination_Nodes_T.DEST_ID, d.getId());
            values.put(Destination_Nodes_T.NODE, nodes.get(i).getId());

            long results = db.insert(Destination_Nodes_T.TABLE_NAME, null, values);

            if (results == FAILURE)
                return FAILURE;
        }

        return SUCCESS;
    }

    public static List<Destination> getDestinations() {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNodes() can execute.");
            return null;
        }

        // get all visible Node records
        String query = Destinations_T.GET_DESTINATIONS;
        String[] data = {};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        List<Destination> destinations = new ArrayList<>();
        while(c.moveToNext()) {
            // build the Node object
            int id = c.getInt(c.getColumnIndex(Destinations_T._ID));
            String destName = c.getString(c.getColumnIndex(Destinations_T.NAME));
            destinations.add(new Destination(id, destName , null)); // TODO add it to the list
        }

        c.close();

        return edges;
    }

    public static Destination getDestOfNode(Node n) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getDestOfNode(Node) can execute.");
            return null;
        }if (db == null) {
            Log.e(TAG, "DB must be opened before getNode(int) can execute.");
            return null;
        }

        // get the Node record
        String query = Destination_Nodes_T.GET_DEST_OF_NODE;
        String[] data = {n.getId() + ""};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(Destinations_T._ID));
        String name = c.getString(c.getColumnIndex(Destinations_T.NAME));
        List<Node> nodes = getNodesForDest(id);

        c.close();

        // build the Node object
        return new Destination(id, name, nodes);
    }

    public static List<Node> getNodesForDest(int id) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNodesForDest(int) can execute.");
            return null;
        }

        String query = Destination_Nodes_T.GET_NODES_FOR_DEST;
        String[] data = {id + ""};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;

        List<Node> nodes = new ArrayList<>();
        while(c.moveToNext()) {
            // build the Node object
            int node_id = c.getInt(c.getColumnIndex(Destination_Nodes_T.NODE));
            nodes.add(getNode(node_id)); // add it to the list
        }
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
