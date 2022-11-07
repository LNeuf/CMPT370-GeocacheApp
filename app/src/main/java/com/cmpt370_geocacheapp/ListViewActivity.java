package com.cmpt370_geocacheapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private ArrayList<ListItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        listView = findViewById(R.id.cacheListView);
        listViewAdapter = new ListViewAdapter(this,items);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id){
                Toast.makeText((Context) ListViewActivity.this,
                        (CharSequence) items.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        listViewAdapter.notifyDataSetChanged();
    }


}
