package edu.bridgewater.csci400.mapper.db;

import android.provider.BaseColumns;

public class Edges_T implements BaseColumns {
    private Edges_T() {} // makes class uninstantiable

    public static final String TABLE_NAME = "Edges";

    // fields
    // automatically get the ID Edges from Basecolumns
    public static final String NODE_1 = "Node_1";
    public static final String NODE_2 = "Node_2";

    // SQL statements
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY," +
            NODE_1 + " INTEGER NOT NULL REFERENCES " + Nodes_T.TABLE_NAME + "(" + Nodes_T._ID + ") ON UPDATE CASCADE," +
            NODE_2 + " INTEGER NOT NULL REFERENCES " + Nodes_T.TABLE_NAME + "(" + Nodes_T._ID + ") ON UPDATE CASCADE)";

    public static final String DELETE_TABLE = "DELETE TABLE IF EXISTS " + TABLE_NAME;

    public static final String GET_EDGE = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = ?";

    public static final String GET_EDGES = "SELECT * FROM " + TABLE_NAME;

    public static final String GET_EDGES_WITH_NODE = "SELECT * FROM " + TABLE_NAME + " WHERE " +
            NODE_1 + " = ? OR " + NODE_2 + " = ?";
}
