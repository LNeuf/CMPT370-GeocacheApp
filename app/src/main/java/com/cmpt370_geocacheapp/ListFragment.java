package com.cmpt370_geocacheapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.stream.Collectors;


public class ListFragment extends Fragment implements ModelListener, IModelListener{
    ListView lv;

    ArrayAdapter<String> adapter;
    String[] cacheNames = {};

    ApplicationController controller;
    ApplicationModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        lv = view.findViewById(R.id.cacheListView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cacheNames);
        lv.setAdapter(adapter);

        return view;
    }


    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

    public void setModel(ApplicationModel newModel)
    {
        this.model = newModel;
    }

    @Override
    public void iModelChanged() {

    }

    @Override
    public void modelChanged() {
        // update list of filtered cache names from model
        this.cacheNames = this.model.getFilteredCacheList().stream().map(GeoCache::getCacheName).toArray(String[]::new);
    }
}