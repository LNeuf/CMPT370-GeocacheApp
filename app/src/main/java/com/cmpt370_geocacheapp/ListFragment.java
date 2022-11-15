package com.cmpt370_geocacheapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class ListFragment extends Fragment implements ModelListener, IModelListener{
    ListView lv;

    private ListViewAdapter listViewAdapter;
    private ArrayList<ListItem> items = new ArrayList<>();
    String[] cacheNames = {};

    ApplicationController controller;
    ApplicationModel model;
    InteractionModel iModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        lv = view.findViewById(R.id.cacheListView);
        listViewAdapter = new ListViewAdapter(this.getContext(),items);
        lv.setAdapter(listViewAdapter);

        lv.setOnItemClickListener(this::handleCacheNameSelected);

        return view;
    }

    private void handleCacheNameSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // need to make selected cache the InteractionModels selected cache
        PhysicalCacheObject selectedCache = model.getFilteredCacheList().get(i);
        controller.setSelectedCache(selectedCache);
    }


    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

    public void setModel(ApplicationModel newModel)
    {
        this.model = newModel;
    }

    public void setIModel(InteractionModel newIModel)
    {
        this.iModel = newIModel;
    }

    @Override
    public void iModelChanged() {
        // update distances
        if (iModel.getCurrentLocation() != null)
        {
            for(ListItem item : items)
            {
                item.setCacheDistance(calculateCacheDistance(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude(), item.getLatitude(), item.getLongitude()));
            }

            // refresh fragment data
            ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();

        }
    }

    @Override
    public void modelChanged() {

        // create the listview items from current filtered caches - with with or without distance data
        this.items = (ArrayList<ListItem>) this.model.getFilteredCacheList().stream().map(cacheObject ->
                new ListItem(cacheObject.getCacheName(), cacheObject.getCacheSummary(), "Time", String.valueOf(cacheObject.getCacheID()),
                        (iModel.getCurrentLocation() != null ? calculateCacheDistance(cacheObject) : "Distance not available."),
                        cacheObject.getCacheLatitude(), cacheObject.getCacheLongitude())).collect(Collectors.toList());


        if (listViewAdapter != null) {
            listViewAdapter = new ListViewAdapter(this.getContext(),items);
            lv.setAdapter(listViewAdapter);
        }
    }

    private String calculateCacheDistance(PhysicalCacheObject cache)
    {
        double calculatedDistance = SphericalUtil.computeDistanceBetween(new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude()), new LatLng(cache.getCacheLatitude(), cache.getCacheLongitude()));

        return (int)calculatedDistance + " m";
    }

    private String calculateCacheDistance(double lat1, double long1, double lat2, double long2)
    {
        double calculatedDistance = SphericalUtil.computeDistanceBetween(new LatLng(lat1, long1), new LatLng(lat2, long2));

        return (int)calculatedDistance + " m";
    }
}