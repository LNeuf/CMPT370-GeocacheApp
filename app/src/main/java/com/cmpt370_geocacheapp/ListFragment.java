package com.cmpt370_geocacheapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ListFragment extends Fragment {
    ListView lv;

    ArrayAdapter<String> adapter;
    String[] testData = {"Cache 1", "Cache 2", "Cache 3", "Cache 4", "Cache 5"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        lv = view.findViewById(R.id.cacheListView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, testData);
        lv.setAdapter(adapter);

        return view;
    }
}