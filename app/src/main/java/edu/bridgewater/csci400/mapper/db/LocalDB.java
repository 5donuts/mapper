package edu.bridgewater.csci400.mapper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static Edge getEdgeWithNodes(Node n1, Node n2) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getEdgeWithNodes(Node, Node) can execute.");
            return null;
        }
        List<Edge> edges = getEdgesWithNode(n1);
        for(Edge e : edges) {
            if(e.contains(n2))
                return e;
        }
        return null;
    }

    public static List<Edge> getEdgesWithNode(Node n) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getEdgesWithNode(Node) can execute.");
            return null;
        }
        String query = Edges_T.GET_EDGES_WITH_NODE;
        String[] data = {n.getId() + "", n.getId() + ""};
        Cursor c = db.rawQuery(query, data);
        if(c == null || c.getCount() == 0)
            return null;

        List<Edge> edges = new ArrayList<>();
        while(c.moveToNext()) {
            // build the corresponding Node objects
            int nodeId1 = c.getInt(c.getColumnIndex(Edges_T.NODE_1));
            int nodeId2 = c.getInt(c.getColumnIndex(Edges_T.NODE_2));
            Node node1 = getNode(nodeId1);
            Node node2 = getNode(nodeId2);

            // build the Edge object
            int id = c.getInt(c.getColumnIndex(Edges_T._ID));
            edges.add(new Edge(id, node1, node2));
        }

        c.close();

        return edges;
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
        values.put(Nodes_T.DEST_ID, n.getDestId());

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
        int destId = c.getInt(c.getColumnIndex(Nodes_T.DEST_ID));

        c.close();

        // build the Node object
        return new Node(id, new LatLng(lat, lon), destId);
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

        // get all Node records
        String query = Nodes_T.GET_NODES;
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
            int destId = c.getInt(c.getColumnIndex(Nodes_T.DEST_ID));
            nodes.add(new Node(id, new LatLng(lat, lon), destId)); // add it to the list
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
        values.put(Destinations_T.LATITUDE, d.getDestPin().latitude);
        values.put(Destinations_T.LONGITUDE, d.getDestPin().longitude);

        long results = db.insert(Destinations_T.TABLE_NAME, null, values);
        return results == -1 ? FAILURE : SUCCESS;
    }

    public static List<Destination> getDestinations() {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNodes() can execute.");
            return null;
        }

        // get all Destination records
        String query = Destinations_T.GET_DESTINATIONS;
        String[] data = {};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        List<Destination> destinations = new ArrayList<>();
        while(c.moveToNext()) {
            // build the Destination object
            int id = c.getInt(c.getColumnIndex(Destinations_T._ID));
            String destName = c.getString(c.getColumnIndex(Destinations_T.NAME));
            double lat = c.getDouble(c.getColumnIndex(Destinations_T.LATITUDE));
            double lon = c.getDouble(c.getColumnIndex(Destinations_T.LONGITUDE));
            // TODO don't have a null nodes list here
            destinations.add(new Destination(id, destName, new LatLng(lat, lon)));
        }

        c.close();

        return destinations;
    }

    public static Destination getDestOfNode(Node n) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getDestOfNode(Node) can execute.");
            return null;
        }if (db == null) {
            Log.e(TAG, "DB must be opened before getDestOfNode(int) can execute.");
            return null;
        }

        // get the Destination record
        String query = Destinations_T.GET_DESTINATION;
        String[] data = {n.getDestId() + ""};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;
        c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(Destinations_T._ID));
        String name = c.getString(c.getColumnIndex(Destinations_T.NAME));
        double lat = c.getDouble(c.getColumnIndex(Destinations_T.LATITUDE));
        double lon = c.getDouble(c.getColumnIndex(Destinations_T.LONGITUDE));
//        List<Node> nodes = getNodesForDest(id); // TODO revisit this

        c.close();

        // build the Destination object
        return new Destination(id, name, new LatLng(lat, lon));
    }

    public static List<Node> getNodesForDest(int id) {
        if (db == null) {
            Log.e(TAG, "DB must be opened before getNodesForDest(int) can execute.");
            return null;
        }

        String query = Nodes_T.GET_NODES_FOR_DEST;
        String[] data = {id + ""};
        Cursor c = db.rawQuery(query, data);
        if (c == null || c.getCount() == 0)
            return null;

        List<Node> nodes = new ArrayList<>();
        while(c.moveToNext()) {
            // build the Node object
            int nodeId = c.getInt(c.getColumnIndex(Nodes_T._ID));
            double lat = c.getDouble(c.getColumnIndex(Nodes_T.LATITUDE));
            double lon = c.getDouble(c.getColumnIndex(Nodes_T.LONGITUDE));
            int destId = c.getInt(c.getColumnIndex(Nodes_T.DEST_ID));
            nodes.add(new Node(nodeId, new LatLng(lat, lon), destId)); // add it to the list
        }
        return nodes;
    }

    private static void readFromJson(Context context) {
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(context.getAssets().open("map_data.json"));
            reader = new BufferedReader(is);

            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }

            String json = out.toString();
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

            JsonArray destinations = jsonObject.getAsJsonObject("map_data").getAsJsonArray("destinations");
            for(int i = 0; i < destinations.size(); i++) {
                int id = destinations.get(i).getAsJsonObject().get("id").getAsInt();
                String name = destinations.get(i).getAsJsonObject().get("name").getAsString();
                LatLng pos = new LatLng(destinations.get(i).getAsJsonObject().get("latitude").getAsDouble(), destinations.get(i).getAsJsonObject().get("longitude").getAsDouble());
                addDestination(new Destination(id, name, pos));
            }

            JsonArray nodes = jsonObject.getAsJsonObject("map_data").getAsJsonArray("nodes");
            for (int i = 0; i < nodes.size(); i++) {
                int id =  nodes.get(i).getAsJsonObject().get("id").getAsInt();
                int destId = nodes.get(i).getAsJsonObject().get("destination_id").getAsInt();
                LatLng pos = new LatLng(nodes.get(i).getAsJsonObject().get("latitude").getAsDouble(), nodes.get(i).getAsJsonObject().get("longitude").getAsDouble());
                addNode(new Node(id, pos, destId));
            }

            JsonArray edges = jsonObject.getAsJsonObject("map_data").getAsJsonArray("edges");
            for (int i = 0; i < edges.size(); i++) {
                Node node1 = getNode(edges.get(i).getAsJsonObject().get("node1").getAsInt());
                Node node2 = getNode(edges.get(i).getAsJsonObject().get("node2").getAsInt());
                addEdge(new Edge(i, node1, node2));
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
    }

    /**
     * Helper class that sets up the database/upgrades it
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        private Context c;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            c = context;
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(Destinations_T.CREATE_TABLE);
            database.execSQL(Nodes_T.CREATE_TABLE);
            database.execSQL(Edges_T.CREATE_TABLE);
            db = database;
            readFromJson(c);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application database from version " + oldVersion + " to " +
                    newVersion + ". This will destroy all old data.");

            database.execSQL(Edges_T.DELETE_TABLE);
            database.execSQL(Nodes_T.DELETE_TABLE);
            database.execSQL(Destinations_T.DELETE_TABLE);

            database.execSQL(Destinations_T.CREATE_TABLE);
            database.execSQL(Nodes_T.CREATE_TABLE);
            database.execSQL(Edges_T.CREATE_TABLE);
            db = database;

            readFromJson(c);
        }
    }
}
