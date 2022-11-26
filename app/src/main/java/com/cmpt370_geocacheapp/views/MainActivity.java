package com.cmpt370_geocacheapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.Manifest;
import android.content.pm.PackageManager;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.databinding.ActivityHomeBinding;
import com.cmpt370_geocacheapp.model.ApplicationModel;
import com.cmpt370_geocacheapp.imodel.IModelListener;
import com.cmpt370_geocacheapp.imodel.InteractionModel;
import com.cmpt370_geocacheapp.model.ModelListener;
import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.cmpt370_geocacheapp.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IModelListener, ModelListener, OnMyLocationButtonClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private ActivityHomeBinding binding;
    private ListFragment listFragment;
    private CacheCreateFragment cacheCreateFragment;
    private RecommendCacheFragment recommendCacheFragment;
    private FilterCacheFragment filterCacheFragment;
    private SearchCacheFragment searchCacheFragment;
    private DetailCacheFragment detailCacheFragment;
    private ApplicationController controller;
    private ApplicationModel model;
    private InteractionModel iModel;
    private GoogleMap gMap;
    boolean requestingLocationUpdates = false;
    private FusedLocationProviderClient client;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private boolean receivedFirstLocationUpdate = false;

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
        filterCacheFragment = new FilterCacheFragment();
        searchCacheFragment = new SearchCacheFragment();
        detailCacheFragment = new DetailCacheFragment();


        // MVC linking
        listFragment.setModel(model);
        cacheCreateFragment.setModel(model);
        cacheCreateFragment.setIModel(iModel);
        listFragment.setIModel(iModel);
        listFragment.setController(controller);
        cacheCreateFragment.setController(controller);
        recommendCacheFragment.setController(controller);
        filterCacheFragment.setController(controller);
        searchCacheFragment.setController(controller);
        detailCacheFragment.setController(controller);
        detailCacheFragment.setModel(model);
        detailCacheFragment.setIModel(iModel);

        model.addSubscriber(listFragment);
        model.addSubscriber(this);
        iModel.addSubscriber(listFragment);
        iModel.addSubscriber(this);

        // initialize model database
        model.initDatabase(this.getApplicationContext());

        // add all fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, listFragment);
        fragmentTransaction.add(R.id.frame_layout, cacheCreateFragment);
        fragmentTransaction.add(R.id.frame_layout, recommendCacheFragment);
        fragmentTransaction.add(R.id.frame_layout, filterCacheFragment);
        fragmentTransaction.add(R.id.frame_layout, searchCacheFragment);
        fragmentTransaction.add(R.id.frame_layout, detailCacheFragment);
        fragmentTransaction.commit();

        // hide list and create fragments on startup - Google Map is always shown below
        hideFragment(listFragment);
        hideFragment(cacheCreateFragment);
        hideFragment(filterCacheFragment);
        hideFragment(recommendCacheFragment);
        hideFragment(searchCacheFragment);
        hideFragment(detailCacheFragment);

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
                    showFragment(cacheCreateFragment);
                    hideFragment(listFragment);
                    break;
            }
            return true;
        });

        // setup toolbar filter and recommend items
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.filter_caches:
                    showFragment(filterCacheFragment);
                    hideFragment(recommendCacheFragment);
                    hideFragment(searchCacheFragment);
                    break;
                case R.id.recommend_caches:
                    showFragment(recommendCacheFragment);
                    hideFragment(filterCacheFragment);
                    hideFragment(searchCacheFragment);
                    break;
                case R.id.search_caches:
                    showFragment(searchCacheFragment);
                    hideFragment(filterCacheFragment);
                    hideFragment(recommendCacheFragment);
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
                controller.setCurrentLocation(locationResult.getLastLocation());
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

        // Google map event handling
        gMap.setOnInfoWindowClickListener(this::infoWindowClicked);
        gMap.setOnInfoWindowLongClickListener(this::infoWindowLongClicked);
        gMap.setOnMyLocationButtonClickListener(this);
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
        controller.setSelectedCache(null);
    }

    /**
     * Handles when the "Current location" button is clicked on the google map
     *
     * @return - returns false to no prevent the default behaviour
     */
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
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
        Toast.makeText(this, "Navigating to cache", Toast.LENGTH_SHORT).show();
        PhysicalCacheObject clickedCache = null;
        for (PhysicalCacheObject cache : model.getFilteredCacheList()) {
            if (String.valueOf(cache.getCacheID()).equals(marker.getSnippet()))
                clickedCache = cache;
        }
        if (clickedCache != null)
            controller.setSelectedCache(clickedCache);
    }

    /**
     * Handles a long press on the info window - opens a detail page with the cache details and comemnts/ratings
     *
     * @param marker - The marker that was long-pressed
     */
    private void infoWindowLongClicked(Marker marker) {
        // this needs to open a detail page
        detailCacheFragment.setCacheInfo(marker.getSnippet());
        showFragment(detailCacheFragment);
    }

    /**
     * Imodel changed - selected cache, location or both has changed
     */
    @Override
    public void iModelChanged() {
        if (iModel.getCurrentLocation() != null && !receivedFirstLocationUpdate) {
            // setting map to show first received location - center map on user
            setupMapAndCachesFromLocation();
        }

        if (iModel.isSelectedCachedChanged()) {
            if (iModel.getCurrentlySelectedCache() != null) {
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(iModel.getCurrentlySelectedCache().getCacheLatitude(),
                                iModel.getCurrentlySelectedCache().getCacheLongitude()), 17));
                // hide other fragments
                hideFragment(listFragment);
                hideFragment(cacheCreateFragment);
                // need to select map nav button
                binding.bottomNavigationView.setSelectedItemId(R.id.filter_caches);

                List<LatLng> newPoints = new ArrayList<LatLng>() {
                    {
                        add(new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude()));
                        add(new LatLng(iModel.getCurrentlySelectedCache().getCacheLatitude(), iModel.getCurrentlySelectedCache().getCacheLongitude()));
                    }
                };
                iModel.getCurrentCacheLine().setPoints(newPoints);
            } else {
                // deselected cache, remove polyline points
                iModel.getCurrentCacheLine().setPoints(new ArrayList<>());
            }

        }
    }

    /**
     * Initializes the model with caches nearby starting location
     */
    private void setupMapAndCachesFromLocation() {
        // Set camera on user position
        CameraPosition currentPos = CameraPosition.builder().target(new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude())).zoom(18).build();
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPos));
        // populate map with nearby caches
        model.updateNearbyCacheList((float) iModel.getCurrentLocation().getLatitude(), (float) iModel.getCurrentLocation().getLongitude(), 5000);
        redrawMapItems();

        receivedFirstLocationUpdate = true;
    }

    /**
     * Cache items have changed - need to redraw map items
     */
    @Override
    public void modelChanged() {
        redrawMapItems();
    }

    /**
     * Re-draws polyline and all markers of the google map
     */
    private void redrawMapItems() {
        // populate map with location markers and cache line
        if (gMap != null) {
            gMap.clear();
            Polyline lineToCache = gMap.addPolyline(new PolylineOptions().clickable(true));
            controller.setCurrentCacheLine(lineToCache);
            for (PhysicalCacheObject cache : this.model.getFilteredCacheList()) {
                gMap.addMarker(new MarkerOptions()
                        .position(new LatLng(cache.getCacheLatitude(), cache.getCacheLongitude()))
                        .title(cache.getCacheName() + "\n" + cache.getCacheSummary())
                        .icon(getBitmapDescriptorFromVector(this, R.drawable.test_icon))
                        .snippet(String.valueOf(cache.getCacheID())));

            }
        }
    }

    /**
     * Takes a context and vector id and generates a BitmapDescriptor for use as an icon in a google map
     */
    private BitmapDescriptor getBitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // check if permissions are granted, then enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            requestingLocationUpdates = true;
            return;
        }

        // Request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
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
            permissionDenied = true;
        }
    }

    /**
     * When app resumes resume location updates
     */
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

    /**
     * Restarts location updates when app resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        // resumes getting location updates if app regains focus
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Pauses location updates when app get paused (moves to background)
     */
    @Override
    protected void onPause() {
        super.onPause();
        // stops location updates from running when app doesn't have focus
        stopLocationUpdates();
    }

}