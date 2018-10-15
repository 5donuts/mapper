package edu.bridgewater.csci400.mapper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
