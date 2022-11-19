package com.cmpt370_geocacheapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.cmpt370_geocacheapp.database.AppDatabase;
import com.cmpt370_geocacheapp.database.CommentDao;
import com.cmpt370_geocacheapp.database.Geocache;
import com.cmpt370_geocacheapp.database.GeocacheDao;
import com.cmpt370_geocacheapp.database.UserDao;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationModel {
    // Geocache attributes
    private ArrayList<PhysicalCacheObject> unfilteredCacheList;
    private ArrayList<PhysicalCacheObject> filteredCacheList;
    private ArrayList<ModelListener> subscribers;
    AtomicInteger fakeCacheID = new AtomicInteger(); // TODO: remove when switching to actual cache database
    private UserDao userDao;
    private GeocacheDao geocacheDao;
    private AppDatabase db;
    private CommentDao commentDao;

    /**
     * Constructor of the applications model
     */
    public ApplicationModel() {
        this.unfilteredCacheList = new ArrayList<>();
        this.filteredCacheList = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public void initDatabase(Context context) {
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build(); // TODO: currently uses main thread to build DB - should probably change so it doesn't lock up the UI
        geocacheDao = db.geocacheDao();
        userDao = db.userDao();
        commentDao = db.commentDao();
        try {
            loadFakeData(context);
        }
        catch (IOException e)
        {
            Toast.makeText(context, "Error loading database data?", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFakeData(Context context) throws IOException {
        InputStream is = context.getAssets().open("caches_tab.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> lines = (ArrayList<String>) reader.lines().collect(Collectors.toList());

        for (String line : lines)
        {
            String[] items = line.split("\t");
            try {
                Geocache c = new Geocache();
                c.id = Integer.parseInt(items[2]);
                c.cacheName = items[0];
                c.userUsername = items[1];
                c.latitude = Float.parseFloat(items[3]);
                c.longitude = Float.parseFloat(items[4]);
                c.cacheDiff = Integer.parseInt(items[5]);
                c.terrainDiff = Integer.parseInt(items[6]);
                c.cacheSize = Integer.parseInt(items[7]);
                geocacheDao.insertAll(c);
            } catch (NumberFormatException e)
            {
                Log.d("database", "loadFakeData: create cache failed");
            }
        }


    }

    public ArrayList<PhysicalCacheObject> getFilteredCacheList() {
        return filteredCacheList;
    }


    public void updateNearbyCacheList(float latitude, float longitude, float distance) {
        // Latitude: 1 deg = 110.574 km
        // Longitude: 1 deg = 111.320*cos(latitude) km

        double distanceInLatitudeDegrees = (distance / 1000) /110.574;
        double distanceInLongitudeDegrees = (distance / 1000) / (111.320 * Math.cos(Math.toRadians(latitude)));
        List<Geocache> cacheList = geocacheDao.getByLatLong(latitude - distanceInLatitudeDegrees,
                latitude + distanceInLatitudeDegrees,  longitude - distanceInLongitudeDegrees,
                longitude + distanceInLongitudeDegrees); // get db geocache objects
        for (Geocache dbCache : cacheList)
        {
            CacheObject cacheObject = new CacheObject(dbCache.cacheName, new User(dbCache.userUsername, "123", 12345), dbCache.id);
            this.unfilteredCacheList.add(new PhysicalCacheObject(cacheObject, dbCache.latitude, dbCache.longitude, dbCache.cacheDiff, dbCache.terrainDiff, dbCache.cacheSize));
        }
        this.filteredCacheList = unfilteredCacheList;
        notifySubscribers();
    }

    /**
     * Method to update the filtered cache list based on a list of predicates
     *
     * @param filterList - The list of filters/predicates to filter the cache list by
     */
    public void updateFilteredCacheList(ArrayList<Predicate<PhysicalCacheObject>> filterList) {
        List<PhysicalCacheObject> result = this.unfilteredCacheList; // gets the current list of nearby caches
        for (Predicate<PhysicalCacheObject> filter : filterList) {
            result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter
        }

        // convert filtered caches to arraylist and update
        this.filteredCacheList = (ArrayList<PhysicalCacheObject>) result;
        notifySubscribers();
    }

    public void filterCachesByDistance(double currentLatitude, double currentLongitude, int distanceInMeters)
    {
        List<PhysicalCacheObject> result = this.filteredCacheList; // gets the current already filtered cache list
        Predicate<PhysicalCacheObject> filter = cacheObject -> distanceInMeters >= calculateCacheDistance(currentLatitude, currentLongitude, cacheObject);
        result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter

        // convert filtered caches to arraylist and update
        this.filteredCacheList = (ArrayList<PhysicalCacheObject>) result;
        notifySubscribers();
    }

    private int calculateCacheDistance(double currentLatitude, double currentLongitude, PhysicalCacheObject cacheObject) {
        return (int) SphericalUtil.computeDistanceBetween(new LatLng(currentLatitude, currentLongitude), new LatLng(cacheObject.getCacheLatitude(), cacheObject.getCacheLongitude()));
    }


    /**
     * Sets a randomly selected nearby cache that meets filter criteria to be recommended
     *
     * @param filterList - the filter criteria to meet
     * @return - Returns a geocache from the nearby cache list that meets criteria, otherwise returns null if no caches meet criteria
     */
    public PhysicalCacheObject getRecommendedCache(ArrayList<Predicate<PhysicalCacheObject>> filterList, LatLng currentLocation, int maxDistance) {
        List<PhysicalCacheObject> result = this.unfilteredCacheList; // gets the current list of nearby caches
        for (Predicate<PhysicalCacheObject> filter : filterList) {
            result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter
        }

        // factor in distance if provided a location
        if (currentLocation != null)
        {
            List<PhysicalCacheObject> distanceFilteredResult = result; // gets the current already filtered cache list
            Predicate<PhysicalCacheObject> distanceFilter = cacheObject -> maxDistance >= calculateCacheDistance(currentLocation.latitude, currentLocation.longitude, cacheObject);
            distanceFilteredResult = distanceFilteredResult.stream().filter(distanceFilter).collect(Collectors.toList()); // apply distance filter

            // convert filtered caches to arraylist and update
            result = distanceFilteredResult;
        }

        // Return random cache that meets criteria, otherwise return null if no caches meet criteria
        if (result.size() > 1) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(result.size());
            return result.get(randomIndex);
        } else if (result.size() == 1)
            return result.get(0);
        else
            return null;

    }

    public PhysicalCacheObject SearchByID(String cacheID) {
        // TODO: search whole DB for specific cache ID
        return null;
    }

    /**
     * Adds a new subscriber to the list of subscribers
     *
     * @param sub - The new subscriber
     */
    public void addSubscriber(ModelListener sub) {
        this.subscribers.add(sub);
    }

    /**
     * Notifies all subscribers that the model has changed
     */
    public void notifySubscribers() {
        for (ModelListener sub : subscribers) {
            sub.modelChanged();
        }
    }

    public void init() {
        notifySubscribers();
    }

    public PhysicalCacheObject createNewCache(String cacheName,User cacheCreator,float latitude,float longitude,int cacheDifficulty,int terrainDifficulty,int cacheSize) {
        PhysicalCacheObject newCache = new PhysicalCacheObject(new CacheObject(cacheName, cacheCreator, fakeCacheID.incrementAndGet()),
                latitude, longitude, cacheDifficulty, terrainDifficulty, cacheSize);
        // TODO: Add cache to DB
        // for now will just add it to the unfiltered cache list, and re-run filters
        this.unfilteredCacheList.add(newCache);
        this.updateFilteredCacheList(new ArrayList<>());
        return newCache;
    }
}
