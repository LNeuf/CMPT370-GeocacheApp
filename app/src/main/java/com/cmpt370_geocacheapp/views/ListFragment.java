package com.cmpt370_geocacheapp.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.model.ApplicationModel;
import com.cmpt370_geocacheapp.imodel.IModelListener;
import com.cmpt370_geocacheapp.imodel.InteractionModel;
import com.cmpt370_geocacheapp.model.ModelListener;
import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.cmpt370_geocacheapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class ListFragment extends Fragment implements ModelListener, IModelListener, AdapterView.OnItemSelectedListener {
    ListView lv;
    Spinner sortSpinner;

    private CacheListViewAdapter cacheListViewAdapter;
    private ArrayList<CacheListItem> items = new ArrayList<>();

    ApplicationController controller;
    ApplicationModel model;
    InteractionModel iModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        lv = view.findViewById(R.id.cacheListView);
        cacheListViewAdapter = new CacheListViewAdapter(this.getContext(), items);
        lv.setAdapter(cacheListViewAdapter);

        sortSpinner = view.findViewById(R.id.sortSpinner);

        lv.setOnItemClickListener(this::handleCacheNameSelected);
        sortSpinner.setOnItemSelectedListener(this);

        return view;
    }

    /**
     * Handles selecting a cache from the list
     */
    private void handleCacheNameSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // need to make selected cache the InteractionModels selected cache
        PhysicalCacheObject selectedCache = model.getFilteredCacheList().get(i);
        controller.setSelectedCache(selectedCache);
    }

    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

    public void setModel(ApplicationModel newModel) {
        this.model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        this.iModel = newIModel;
    }

    @Override
    public void iModelChanged() {
        // update distances
        if (iModel.getCurrentLocation() != null) {
            for (CacheListItem item : items) {
                double dist = calculateCacheDistance(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude(), item.getLatitude(), item.getLongitude());
                item.setCacheDistance((int) dist + " m");
            }

            // refresh fragment data
            ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void modelChanged() {
        // create the listview items from current filtered caches - with with or without distance data
        this.items = (ArrayList<CacheListItem>) this.model.getFilteredCacheList().stream().map(cacheObject ->
                new CacheListItem(cacheObject.getCacheName(), cacheObject.getCacheSummary(), "Time", String.valueOf(cacheObject.getCacheID()),
                        (iModel.getCurrentLocation() != null ? (int) calculateCacheDistance(cacheObject) + " m" : "Distance not available."),
                        cacheObject.getCacheLatitude(), cacheObject.getCacheLongitude())).collect(Collectors.toList());


        if (cacheListViewAdapter != null) {
            cacheListViewAdapter = new CacheListViewAdapter(this.getContext(), items);
            lv.setAdapter(cacheListViewAdapter);
        }
    }

    /**
     * Helper method for calculating cache distance
     * @param cache - the cache to calculate for
     * @return - the distance in meters
     */
    private double calculateCacheDistance(PhysicalCacheObject cache) {

        return SphericalUtil.computeDistanceBetween(new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude()), new LatLng(cache.getCacheLatitude(), cache.getCacheLongitude()));
    }

    /**
     * Helper method for calculating distance from two lat/long pairs
     * @return - The distance in meters
     */
    private double calculateCacheDistance(double lat1, double long1, double lat2, double long2) {

        return SphericalUtil.computeDistanceBetween(new LatLng(lat1, long1), new LatLng(lat2, long2));
    }

    /**
     * Handles selecting a sort method from the spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        long selectedIndex = adapterView.getSelectedItemId();
        controller.sortCaches(selectedIndex);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}