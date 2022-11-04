package com.cmpt370_geocacheapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap gMap;
    MapView mapView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        gMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng uSask = new LatLng(52.1334, -106.6314); //Set coordinates @Usask
        //Init marker options
        MarkerOptions markerOptions = new MarkerOptions();
        //set position of marker
        markerOptions.position(uSask);
        //set title of marker
        markerOptions.title("Usask Campus");
        //add marker on map
        googleMap.addMarker(markerOptions);
        //Animating to zoom the marker
        CameraPosition Campus = CameraPosition.builder().target(uSask).zoom(16).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Campus));
    }


}