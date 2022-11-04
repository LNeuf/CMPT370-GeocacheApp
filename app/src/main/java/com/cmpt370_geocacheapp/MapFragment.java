package com.cmpt370_geocacheapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback, ModelListener, IModelListener {

    GoogleMap gMap;
    MapView mapView;
    View view;
    ApplicationController controller;
    ApplicationModel model;
    InteractionModel iModel;

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

        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.setInfoWindowAdapter(new CustomInfoWindow(this.getContext()));

        //Animating to zoom the marker
        // TODO: Have map zoom to user location if available, otherwise zoom to default location of USASK
        LatLng usask = new LatLng(52.1334, -106.6314); //Set coordinates @Usask
        CameraPosition currentPos = CameraPosition.builder().target(usask).zoom(18).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPos));

        // populate map with location markers
        for (GeoCache cache : this.model.getFilteredCacheList())
        {
            gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(cache.getLatitude(), cache.getLongitude()))
                    .title(cache.getCacheName())
                    .snippet(cache.getCacheID()));

        }

        gMap.setOnMarkerClickListener(this::markerCLicked);

    }

    private boolean markerCLicked(Marker marker) {
        //TODO: Make click on marker bring to cache detail page? Toast placeholder below
        Toast.makeText(this.getContext(), "MarkerID: " + marker.getSnippet(), Toast.LENGTH_SHORT).show();
        return false;
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
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
        // selected new cache, move map to cache and zoom in on it
        if (iModel.getCurrentlySelectedCache() != null)
        {
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(iModel.getCurrentlySelectedCache().getLatitude(),
                            iModel.getCurrentlySelectedCache().getLongitude()), 17));

        }
    }

    @Override
    public void modelChanged() {

    }
}