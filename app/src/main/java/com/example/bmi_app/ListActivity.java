package com.example.bmi_app;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);

        ArrayList<HashMap<String,String>> MyArrList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("trans_id", "1");
        map.put("name", "Pakorn");
        map.put("msg", "Junk Food");
        map.put("amt", "300");
        MyArrList.add(map);
        MyArrList.add(map);
        MyArrList.add(map);

        final ListView listView = (ListView)findViewById(R.id.listView);

        SimpleAdapter simpleAdapter = new SimpleAdapter(ListActivity.this, MyArrList,
                R.layout.activity_column, new String[]{"trans_id", "name", "msg", "amt"},
                new int[]{R.id.col_trans_id, R.id.col_name, R.id.col_msg, R.id.col_amt});
        listView.setAdapter(simpleAdapter);
    }
}
