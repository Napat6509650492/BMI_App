package com.example.bmi_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);
        final ListView listView = (ListView)findViewById(R.id.listView);
        ArrayList<HashMap<String,String>> MyArrList = new ArrayList<>();

        String filename = getString(R.string.file_name);
        // Try reading the existing JSON file (if any)
        try (FileInputStream fis = openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)){
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // If the file isn't empty, parse it into a JSONArray
            if (stringBuilder.length() > 0) {
                MyArrList = jsonArrayToArrayList(new JSONArray(stringBuilder.toString()));
                Collections.reverse(MyArrList);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(ListActivity.this, MyArrList,
                    R.layout.list_row, new String[]{"date", "weight", "height", "bmi","status"},
                    new int[]{R.id.date, R.id.weight, R.id.height, R.id.bmiResult,R.id.bmiCategory});
            listView.setAdapter(simpleAdapter);
        } catch (FileNotFoundException e) {
            // Handle file not found (file might not exist yet)
            Log.v("save","File Not Found");
        } catch (IOException | JSONException e) {
            // Handle other exceptions (e.g., IO errors or JSON parsing errors)
            Log.v("save",e.getMessage(),e);
        }catch(Exception e){
            Log.v("save",e.getMessage(),e);
        }

    }

    // Converts a JSONObject to a HashMap
    public static HashMap<String, String> jsonObjectToHashMap(JSONObject jsonObject) throws JSONException {
        HashMap<String, String> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.get(key).toString();
            map.put(key, value);
        }
        return map;
    }

    // Converts a JSONArray to an ArrayList of HashMaps
    public static ArrayList<HashMap<String, String>> jsonArrayToArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, String> map = jsonObjectToHashMap(jsonObject);
            list.add(map);
        }
        return list;
    }
}
