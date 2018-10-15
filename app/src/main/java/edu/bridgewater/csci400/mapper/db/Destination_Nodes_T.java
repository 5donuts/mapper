package edu.bridgewater.csci400.mapper.db;

import android.provider.BaseColumns;

public class Destination_Nodes_T implements BaseColumns {
    private Destination_Nodes_T() {} // makes class uninstantiable

    public static final String TABLE_NAME = "Destination_Nodes";

    // fields
    // automatically get the _ID field from BaseColumns
    public static final String DEST_ID = "Dest_ID";
    public static final String NODE = "Node";

    // SQL statements
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
            _ID + " INTEGER PRIMARY KEY ON UPDATE CASCADE," +
            DEST_ID + " INTEGER NOT NULL REFERENCES " + Destinations_T.TABLE_NAME + "(" + Destinations_T._ID + ")," +
            NODE + " INTEGER NOT NULL REFERENCES " + Nodes_T.TABLE_NAME + "(" + Nodes_T._ID + "))";

    public static final String DELETE_TABLE = "DELETE TABLE IF EXISTS " + TABLE_NAME;

    public static final String GET_NODES_FOR_DEST = "SELECT * FROM " + TABLE_NAME + " WHERE " +
            DEST_ID + " = ?";

    public static final String GET_DEST_OF_NODE = "SELECT * FROM " + TABLE_NAME + " WHERE " +
            NODE + " = ?";
}
