package edu.bridgewater.csci400.mapper.db;

import android.provider.BaseColumns;

public class Destinations implements BaseColumns {
    private Destinations(){
    }

    public static final String TABLE_NAME = "Destinations";

    public static final String Name = "Name";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS" +
            TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY ON UPDATE CASCADE," +
            Name + " VARCHAR(100) NOT NULL )";

    public static final String DELETE_TABLE = " DELETE TABLE IF EXISTS " + TABLE_NAME;

    public static final String GET_DESTINATION =" SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " =?";
}
