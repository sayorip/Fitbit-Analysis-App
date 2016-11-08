package com.example.pragati.simplebargraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainListView extends Activity {

    ListView lv;
    public ArrayList<HashMap<String, String>> my_menu = new ArrayList<HashMap<String, String>>();
    String[] from = {"menu_name", "menu_desc"};
    int[] to = {R.id.menu_name, R.id.menu_desc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        lv = (ListView) findViewById(R.id.listView);

        HashMap<String, String> map = new HashMap<String, String>();
        map.clear();
        map.put("menu_name", "Sleep");
        map.put("menu_desc", "Sleep Analysis");
        my_menu.add(map);

        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.clear();
        map1.put("menu_name", "Activity");
        map1.put("menu_desc", "Activity Analysis");
        my_menu.add(map1);

        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.clear();
        map2.put("menu_name", "Calories");
        map2.put("menu_desc", "Calories Analysis");
        my_menu.add(map2);
//            }
        ListAdapter adapter = new SimpleAdapter(MainListView.this, my_menu, R.layout.list_fragment, from, to);
        lv.setAdapter(adapter);



        // register onClickListener to handle click events on each item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                String s = "Position" + position;
                switch (position)
                {
                    case 0:
                        Intent intent1 = new Intent(MainListView.this, Sleep.class);
                        startActivity(intent1);
                        break;

                    case 1:
                        Intent intent2 = new Intent(MainListView.this, Activeness.class);
                        startActivity(intent2);
                        break;

                    case 2:
                        Intent intent3 = new Intent(MainListView.this, Calories.class);
                        startActivity(intent3);
                        break;

                }

            }
        });
    }

}
