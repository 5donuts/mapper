package edu.bridgewater.csci400.mapper.db;

import android.provider.BaseColumns;

public class Nodes_T implements BaseColumns {
    private Nodes_T() {} // makes class uninstantiable

    public static final String TABLE_NAME = "Nodes";

    // fields
    // automatically get the _ID field from BaseColumns
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String VISIBLE = "visible";

    // SQL statements
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
            _ID + " INTEGER PRIMARY KEY ON UPDATE CASCADE," +
            LATITUDE + " DOUBLE NOT NULL," +
            LONGITUDE + " DOUBLE NOT NULL," +
            VISIBLE + " BOOLEAN NOT NULL)";

    public static final String DELETE_TABLE = "DELETE TABLE IF EXISTS " + TABLE_NAME;

    public static final String GET_NODE = "SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " = ?";

    public static final String GET_VISIBLE_NODES = "SELECT * FROM " + TABLE_NAME + " WHERE "
            + VISIBLE + " = 1";
}
