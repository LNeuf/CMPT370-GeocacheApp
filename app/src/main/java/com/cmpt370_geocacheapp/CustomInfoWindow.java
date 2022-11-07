package com.cmpt370_geocacheapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * This is a custom info window for the Google Map markers
 * Will be modified to show the Cache info at a glance
 * Marker snippet will store cacheID to allow for passing into a cache details fragment
 */
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    View myView;

    public CustomInfoWindow(Context context) {
        myView = LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtPickupTitle = myView.findViewById(R.id.txtPickupInfo);
        txtPickupTitle.setText(marker.getTitle());
        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}