package edu.bridgewater.csci400.mapper.db;

import android.provider.BaseColumns;

public class Destinations_T implements BaseColumns {
    private Destinations_T() {} // makes class uninstantiable

    // fields
    // automatically get the _ID field from BaseColumns
    public static final String TABLE_NAME = "Destinations_T";
    public static final String NAME = "Name";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";

    // SQL statements
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY, " +
            LATITUDE + " DOUBLE NOT NULL, " +
            LONGITUDE + " DOUBLE NOT NULL, " +
            NAME + " VARCHAR(100) NOT NULL)";

    public static final String DELETE_TABLE = " DELETE TABLE IF EXISTS " + TABLE_NAME;

    public static final String GET_DESTINATIONS = " SELECT * FROM " + TABLE_NAME;

    public static final String GET_DESTINATION =" SELECT * FROM " + TABLE_NAME + " WHERE " +
            _ID + " =?";
}
