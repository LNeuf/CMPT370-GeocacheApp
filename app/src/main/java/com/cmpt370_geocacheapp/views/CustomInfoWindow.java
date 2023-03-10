package com.cmpt370_geocacheapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmpt370_geocacheapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * This is a custom info window for the Google Map markers
 * Will be modified to show the Cache info at a glance
 * Marker snippet will store cacheID to allow for passing into a cache details fragment
 */
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    View myView;

    /**
     * Constructor to create the InfoWindow
     */
    public CustomInfoWindow(Context context) {
        myView = LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    /**
     * Gets the marker info and assigns it to the custom info window
     * @param marker -The marker that was pressed to display the info window
     */
    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtInfoName = myView.findViewById(R.id.txtInfoName);
        String[] markerTitle = marker.getTitle().split("\n");
        txtInfoName.setText(markerTitle[0]);
        TextView txtInfoDesc = myView.findViewById(R.id.txtInfoDesc);
        txtInfoDesc.setText(markerTitle[1]);
        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}