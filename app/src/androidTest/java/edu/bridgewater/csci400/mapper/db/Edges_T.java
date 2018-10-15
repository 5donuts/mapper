package edu.bridgewater.csci400.mapper.db;

import android.provider.BaseColumns;

public class Edges_T implements BaseColumns {
    private Edges_T() {
    }//makes class uninstantiable

    public static final String TABLE_NAME = "Edges";

    //fields
    //automatically get the ID Edges from Basecolumns
    public static final String NODE_1 = "Node_1";
    public static final String Node_2 = "Node_2";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS" +
            TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY ON UPDATE CASCADE," +
            NODE_1 + " INTEGER NOT NULL REFERENCES NODES(_ID)," +
            Node_2 + " INTEGER NOT NULL REFERENCES NODES(_ID))";

    public static final String DELETE_TABLE = " DELETE TABLE IF EXISTS " + TABLE_NAME;

    public static final String GET_EDGE =" SELECT * FROM " + TABLE_NAME + " WHERE " + _ID + " =?";

    public static final String GET_EDGE_WITH_NODES =" SELECT * FROM " + TABLE_NAME + " WHERE " + NODE_1 + " =?|| " + Node_2 + " =? ";
}