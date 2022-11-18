package com.cmpt370_geocacheapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.Manifest;
import android.content.pm.PackageManager;

import com.cmpt370_geocacheapp.databinding.ActivityHomeBinding;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IModelListener, ModelListener, OnMyLocationButtonClickListener, OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private ActivityHomeBinding binding;
    private ListFragment listFragment;
    private CacheCreateFragment cacheCreateFragment;
    private RecommendCacheFragment recommendCacheFragment;
    private ApplicationController controller;
    private ApplicationModel model;
    private InteractionModel iModel;
    private GoogleMap gMap;
    boolean requestingLocationUpdates = false;
    private FusedLocationProviderClient client;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

        client = new FusedLocationProviderClient(this);

        // setup MVC stuff
        model = new ApplicationModel();
        iModel = new InteractionModel();
        controller = new ApplicationController();
        controller.setModel(model);
        controller.setInteractionModel(iModel);

        // Create all fragments
        listFragment = new ListFragment();
        cacheCreateFragment = new CacheCreateFragment();
        recommendCacheFragment = new RecommendCacheFragment();

        // MVC linking
        listFragment.setModel(model);
        cacheCreateFragment.setModel(model);
        cacheCreateFragment.setIModel(iModel);
        listFragment.setIModel(iModel);
        listFragment.setController(controller);
        cacheCreateFragment.setController(controller);
        recommendCacheFragment.setController(controller);

        model.addSubscriber(listFragment);
        model.addSubscriber(this);
        iModel.addSubscriber(listFragment);
        iModel.addSubscriber(this);

        // initialize model
        model.init();

        // add all fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, listFragment);
        fragmentTransaction.add(R.id.frame_layout, recommendCacheFragment);
        fragmentTransaction.commit();

        // hide list and create fragments on startup - Google Map is always shown below
        hideFragment(listFragment);
        hideFragment(cacheCreateFragment);

        // setup bottom navigation events
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_map:
                    hideFragment(listFragment);
                    hideFragment(cacheCreateFragment);
                    break;
                case R.id.nav_list:
                    showFragment(listFragment);
                    hideFragment(cacheCreateFragment);
                    break;
                case R.id.nav_create:
                    showFragment(recommendCacheFragment);
                    hideFragment(listFragment);
                    break;
            }

            return true;
        });

        // setup location update callback
        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                iModel.setCurrentLocation(locationResult.getLastLocation());
            }
        };

    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Map setup with default settings
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.setInfoWindowAdapter(new CustomInfoWindow(this));
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        enableMyLocation();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }

        // Starting at default location at USASK
        LatLng usask = new LatLng(52.1334, -106.6314); //Set coordinates @Usask
        CameraPosition currentPos = CameraPosition.builder().target(usask).zoom(18).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPos));

        // populate map with test location markers. TODO: replace with nearby caches
        for (PhysicalCacheObject cache : this.model.getFilteredCacheList()) {
            gMap.addMarker(new MarkerOptions()
                    .position(new LatLng(cache.getCacheLatitude(), cache.getCacheLongitude()))
                    .title(cache.getCacheName() + "\n" + cache.getCacheSummary())
                    .snippet(String.valueOf(cache.getCacheID())));

        }

        // Initialize empty cache poly line
        Polyline lineToCache = gMap.addPolyline(new PolylineOptions().clickable(true));
        iModel.setCurrentCacheLine(lineToCache);

        // Google map event handling
//        gMap.setOnMarkerClickListener(this::markerCLicked);
        gMap.setOnInfoWindowClickListener(this::infoWindowClicked);
        gMap.setOnMyLocationButtonClickListener(this);
        gMap.setOnMyLocationClickListener(this);
        gMap.setOnMapLongClickListener(this::mapLongPress);
        gMap.setOnPolylineClickListener(this::lineClicked);
        gMap.setOnMapClickListener(this::mapClicked);


    }

    /**
     * Handles when map background is clicked
     *
     * @param latLng - The unused position of the click
     */
    private void mapClicked(LatLng latLng) {
        // unselect cache
        controller.setSelectedCache(null);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * On polyline clicked show distance to cache
     *
     * @param polyline - The Line between current location and current cache on Google Map
     */
    private void lineClicked(Polyline polyline) {
        double d = SphericalUtil.computeDistanceBetween(polyline.getPoints().get(0), polyline.getPoints().get(1));
        Toast.makeText(this, "Distance to cache: " + (int) d + " meters", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles long presses on the map - cycles between map styles
     *
     * @param latLng - Location of press - unused
     */
    private void mapLongPress(LatLng latLng) {
        if (gMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN)
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else if (gMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE)
            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else if (gMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID)
            gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    /**
     * Shows a fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Hides a fragment
     */
    private void hideFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }


    /**
     * Event handling for when the custom Info Window is clicked
     *
     * @param marker - The marker clicked
     */
    private void infoWindowClicked(Marker marker) {
        // TODO: Make a click on info window bring up cache detail fragment or window
        Toast.makeText(this, "Show cache details for marker: " + marker.getSnippet(), Toast.LENGTH_SHORT).show();
        PhysicalCacheObject clickedCache = null;
        for (PhysicalCacheObject cache : model.getFilteredCacheList()) {
            if (String.valueOf(cache.getCacheID()).equals(marker.getSnippet()))
                clickedCache = cache;
        }
        if (clickedCache != null)
            controller.setSelectedCache(clickedCache);
    }

//    /**
//     * Event handling for when a marker is clicked
//     *
//     * @param marker - The marker
//     * @return - False to keep default behaviour
//     */
//    private boolean markerCLicked(Marker marker) {
//        Toast.makeText(this, "MarkerID: " + marker.getSnippet(), Toast.LENGTH_SHORT).show();
//        return false;
//    }

    @Override
    public void iModelChanged() {
        if (iModel.getCurrentlySelectedCache() != null && iModel.isSelectedCachedChanged()) {
            // New cache was selected, move map to cache and zoom in on it
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(iModel.getCurrentlySelectedCache().getCacheLatitude(),
                            iModel.getCurrentlySelectedCache().getCacheLongitude()), 17));
            // hide other fragments
            hideFragment(listFragment);
            hideFragment(cacheCreateFragment);
            // need to select map nav button
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_map);
        }

        if (iModel.getCurrentlySelectedCache() == null && iModel.isSelectedCachedChanged()) {
            // deselected cache, remove polyline
            iModel.getCurrentCacheLine().setPoints(new ArrayList<>());
        }

        if (iModel.getCurrentLocation() != null && iModel.getCurrentlySelectedCache() != null) {
            // Redraw current cache line
            List<LatLng> newPoints = new ArrayList<LatLng>() {
                {
                    add(new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude()));
                    add(new LatLng(iModel.getCurrentlySelectedCache().getCacheLatitude(), iModel.getCurrentlySelectedCache().getCacheLongitude()));
                }
            };
            iModel.getCurrentCacheLine().setPoints(newPoints);

        }
    }

    @Override
    public void modelChanged() {
        // populate map with location markers
        if (gMap != null) {
            gMap.clear();
            Polyline lineToCache = gMap.addPolyline(new PolylineOptions().clickable(true));
            iModel.setCurrentCacheLine(lineToCache);
            for (PhysicalCacheObject cache : this.model.getFilteredCacheList()) {
                gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(cache.getCacheLatitude(), cache.getCacheLongitude()))
                        .title(cache.getCacheName() + "\n" + cache.getCacheSummary())
                        .snippet(String.valueOf(cache.getCacheID())));

            }
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            requestingLocationUpdates = true;
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
        // [END maps_check_location_permission]
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     * Starts getting location requests after-rechecking permissions
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000);
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * Stops app from getting location updates
     */
    private void stopLocationUpdates() {
        client.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // resumes getting location updates if app regains focus
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stops location updates from running when app doesn't have focus
        stopLocationUpdates();
    }

}