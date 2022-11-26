package com.cmpt370_geocacheapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.cmpt370_geocacheapp.database.AppDatabase;
import com.cmpt370_geocacheapp.database.CommentDao;
import com.cmpt370_geocacheapp.database.Geocache;
import com.cmpt370_geocacheapp.database.GeocacheDao;
import com.cmpt370_geocacheapp.database.Picture;
import com.cmpt370_geocacheapp.database.PictureDao;
import com.cmpt370_geocacheapp.database.RatingReview;
import com.cmpt370_geocacheapp.database.RatingReviewDao;
import com.cmpt370_geocacheapp.database.UserDao;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationModel {
    // Geocache attributes
    private ArrayList<PhysicalCacheObject> unfilteredCacheList;
    private ArrayList<PhysicalCacheObject> filteredCacheList;
    private final ArrayList<ModelListener> subscribers;
    private UserDao userDao; // TODO: Implement user DB
    private GeocacheDao geocacheDao;
    private CommentDao commentDao; // TODO: Implement separate comment DB
    private RatingReviewDao ratingDao;
    private PictureDao pictureDao;

    /**
     * Constructor of the applications model
     */
    public ApplicationModel() {
        this.unfilteredCacheList = new ArrayList<>();
        this.filteredCacheList = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    /**
     * Initializes the database instance and sets up DAO's
     */
    public void initDatabase(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        geocacheDao = db.geocacheDao();
        userDao = db.userDao();
        commentDao = db.commentDao();
        ratingDao = db.reviewDao();
        pictureDao = db.pictureDao();
//        try {
//            loadFakeData(context);
//        }
//        catch (IOException e)
//        {
//            Toast.makeText(context, "Error loading database data?", Toast.LENGTH_SHORT).show();
//        }
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

    /**
     * Returns the list of caches that have ben filtered from the unfiltered list
     *
     * @return - Filtered cache list
     */
    public ArrayList<PhysicalCacheObject> getFilteredCacheList() {
        return filteredCacheList;
    }

    /**
     * Queries the cache database to get all caches in a roughly square region, centered on a specified location
     *
     * @param latitude  - center latitude
     * @param longitude - center longitude
     * @param distance  - side distance
     */
    public void updateNearbyCacheList(float latitude, float longitude, float distance) {
        // Latitude: 1 deg = 110.574 km
        // Longitude: 1 deg = 111.320*cos(latitude) km

        unfilteredCacheList = new ArrayList<>();

        double distanceInLatitudeDegrees = (distance / 1000) / 110.574;
        double distanceInLongitudeDegrees = (distance / 1000) / (111.320 * Math.cos(Math.toRadians(latitude)));
        List<Geocache> cacheList = geocacheDao.getByLatLong(latitude - distanceInLatitudeDegrees,
                latitude + distanceInLatitudeDegrees, longitude - distanceInLongitudeDegrees,
                longitude + distanceInLongitudeDegrees); // get db geocache objects
        for (Geocache dbCache : cacheList) {
            CacheObject cacheObject = new CacheObject(dbCache.cacheName, new User(dbCache.userUsername, "123", 12345), dbCache.id);
            PhysicalCacheObject newCache = new PhysicalCacheObject(cacheObject, dbCache.latitude, dbCache.longitude, dbCache.cacheDiff, dbCache.terrainDiff, dbCache.cacheSize);
            newCache.setCacheRating(dbCache.cacheRating);
            // find pic if there
            byte[] blob = pictureDao.getByCacheID(newCache.getCacheID());
            if (blob != null)
            {
                // convert blob to bitmap
                Bitmap pic = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                newCache.setCachePicture(pic);
            }
            this.unfilteredCacheList.add(newCache);
        }
        this.filteredCacheList = (ArrayList<PhysicalCacheObject>) unfilteredCacheList.clone();
        notifySubscribers();
    }

    /**
     * Method to update the filtered cache list based on a list of predicates (filters)
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

    /**
     * Method to filter caches by distance to a latitude/longitude point
     *
     * @param currentLatitude  - the latitude of the point to calculate distance to
     * @param currentLongitude - the longitude of the point to calculate distance to
     * @param distanceInMeters - the distance caches will be filtered by if they are further
     */
    public void filterCachesByDistance(double currentLatitude, double currentLongitude, int distanceInMeters) {
        List<PhysicalCacheObject> result = this.filteredCacheList; // gets the current already filtered cache list
        Predicate<PhysicalCacheObject> filter = cacheObject -> distanceInMeters >= calculateCacheDistance(currentLatitude, currentLongitude, cacheObject);
        result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter

        // convert filtered caches to arraylist and update
        this.filteredCacheList = (ArrayList<PhysicalCacheObject>) result;
        notifySubscribers();
    }

    /**
     * Calculates the distance between a latitude and longitude to a cache object
     *
     * @param currentLatitude  - point latitude
     * @param currentLongitude - point longitude
     * @param cacheObject      - the cache
     * @return - returns the distance in meters
     */
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
        if (currentLocation != null) {
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

    /**
     * Searches the loaded caches by cache ID and returns a physical cache object
     *
     * @param cacheID - the ID to search for
     * @return - the cache, null if no cache found
     */
    public PhysicalCacheObject searchByCacheID(long cacheID) {
        PhysicalCacheObject foundCache = null;
        for (PhysicalCacheObject cache : unfilteredCacheList) {
            if (cache.getCacheID() == cacheID)
                foundCache = cache;
        }
        return foundCache;
    }

    /**
     * Queries the database to find all caches that match or are close to the input author name string
     *
     * @param searchInput - the search string
     * @return - true if more than once cache matched and was added to the filtered list, false otherwise
     */
    public boolean searchByAuthorName(String searchInput) {
        List<Geocache> foundCaches = geocacheDao.getByAuthorString(searchInput);
        this.filteredCacheList = new ArrayList<>();
        for (PhysicalCacheObject loadedCache : unfilteredCacheList) // TODO: This sucks - maybe find another faster method
        {
            for (Geocache foundCache : foundCaches) {
                if (loadedCache.getCacheID() == foundCache.id) {
                    filteredCacheList.add(loadedCache);
                    break;
                }
            }
        }
        notifySubscribers();
        return filteredCacheList.size() > 0;
    }

    /**
     * Queries the database to find all caches that match or are close to the input cache name string
     *
     * @param searchInput - the search string
     * @return - true if more than once cache matched and was added to the filtered list, false otherwise
     */
    public boolean searchByCacheName(String searchInput) {
        List<Geocache> foundCaches = geocacheDao.getByCacheNameString(searchInput);
        this.filteredCacheList = new ArrayList<>();
        for (PhysicalCacheObject loadedCache : unfilteredCacheList) // TODO: This sucks - maybe find another faster method
        {
            for (Geocache foundCache : foundCaches) {
                if (loadedCache.getCacheID() == foundCache.id) {
                    filteredCacheList.add(loadedCache);
                    break;
                }
            }
        }
        notifySubscribers();
        return filteredCacheList.size() > 0;
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

    /**
     * Creates a new cache, and adds it to the database
     *
     * @param cacheName         - Name of cache
     * @param cacheCreator      - User who created
     * @param latitude          - Latitude of cache
     * @param longitude         - Longitude of cache
     * @param cacheDifficulty   - Difficulty of cache
     * @param terrainDifficulty - Terrain difficulty of cache
     * @param cacheSize-        Size of cache
     * @return - returns the created cache object
     */
    public long createNewCache(String cacheName, User cacheCreator, float latitude, float longitude, int cacheDifficulty, int terrainDifficulty, int cacheSize, Bitmap pic) {
        Geocache c = new Geocache();
        c.cacheName = cacheName;
        c.userUsername = cacheCreator.getUsername();
        c.latitude = latitude;
        c.longitude = longitude;
        c.cacheDiff = cacheDifficulty;
        c.terrainDiff = terrainDifficulty;
        c.cacheSize = cacheSize;
        c.cacheRating = 0.0;
        geocacheDao.insertAll(c);

        List<Geocache> createdCache = geocacheDao.getByLatLong(latitude, latitude, longitude, longitude);
        long cacheID = createdCache.get(0).id;

        Picture p = new Picture();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        p.pictureBlob = bArray;
        p.geocacheId = cacheID;
        pictureDao.insertAll(p);

        return cacheID;

    }

    /**
     * Gets a loaded cache by cacheID
     *
     * @param cacheID - the cache ID to search for
     * @return - returns teh cache object of cache found, null otherwise
     */
    public PhysicalCacheObject getCacheById(long cacheID) {
        for (PhysicalCacheObject cache : unfilteredCacheList) {
            if (cache.getCacheID() == cacheID)
                return cache;
        }
        return null;
    }

    /**
     * Returns a list of ratings associated with a specified cache id
     *
     * @param cacheID - the cache id to get the rating list for
     * @return - Returns a list of ratings for a specified cache
     */
    public List<RatingReview> getCacheRatings(long cacheID) {
        return ratingDao.getByCacheID(cacheID);

    }

    /**
     * Creates a new cache rating/comment
     *
     * @param username          - The user who created the rating
     * @param contents          - The contents of the comment
     * @param rating            - The rating out of five - 1-5 valid
     * @param currentGeocacheID - the cacheID of the associated cache
     */
    public void createNewRating(String username, String contents, int rating, long currentGeocacheID) {
        // add rating
        RatingReview r = new RatingReview();
        r.userUsername = username;
        r.contents = contents;
        r.rating = rating;
        r.geocacheId = currentGeocacheID;
        ratingDao.insertAll(r);

        // Update the cache to store the newest value for average rating
        List<RatingReview> cacheRatings = ratingDao.getByCacheID(currentGeocacheID);
        double total = 0.0;
        for (RatingReview cacheRating : cacheRatings) {
            total += cacheRating.rating;
        }
        Geocache cacheToUpdate = geocacheDao.getByCacheID(currentGeocacheID);
        cacheToUpdate.cacheRating = total / cacheRatings.size();
        geocacheDao.updateAll(cacheToUpdate);
    }

    /**
     * Deletes a cache from the database
     *
     * @param currentGeocacheID - the ID of the cache to delete
     */
    public void deleteCache(long currentGeocacheID) {
        Geocache cacheToDelete = geocacheDao.getByCacheID(currentGeocacheID);
        geocacheDao.deleteAll(cacheToDelete);
    }

    /**
     * Sorts the filtered caches based on the sort method index
     *
     * @param sortMethodIndex - The sort method to use
     */
    public void sortCaches(long sortMethodIndex) {
        /*
        <item>Cache ID: Low to High</item>
        <item>Cache ID: High to Low</item>
        <item>Difficulty: Easy to Hard</item>
        <item>Difficulty: Hard to Easy</item>
        <item>Size: Small to Large</item>
        <item>Size: Large to Small</item>
        <item>Popularity: Low to High</item>
        <item>Popularity: High to Low</item>
         */
        if (sortMethodIndex == 0) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheID));
        } else if (sortMethodIndex == 1) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheID).reversed());
        } else if (sortMethodIndex == 2) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheDifficulty));
        } else if (sortMethodIndex == 3) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheDifficulty).reversed());
        } else if (sortMethodIndex == 4) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheSize));
        } else if (sortMethodIndex == 5) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheSize).reversed());
        } else if (sortMethodIndex == 6) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheRating));
        } else if (sortMethodIndex == 7) {
            filteredCacheList.sort(Comparator.comparing(PhysicalCacheObject::getCacheRating).reversed());
        }

        notifySubscribers();
    }
}
