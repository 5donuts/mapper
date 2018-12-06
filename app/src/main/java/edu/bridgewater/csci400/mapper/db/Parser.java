package edu.bridgewater.csci400.mapper.db;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.IOException;
import android.content.Context;

public class Parser {
    public static void main(String args[]) {

        JSONObject obj = new JSONObject(readJson());
    }

    public static String readJson() {
        String json = null;
        try {
            InputStream is = new FileInputStream("map_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
