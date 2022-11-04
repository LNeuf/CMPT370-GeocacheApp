package com.cmpt370_geocacheapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationModel {
    // Geocache attributes
    private ArrayList<GeoCache> unfilteredCacheList;
    private ArrayList<GeoCache> filteredCacheList;
    private GeoCache recommendedCache;
    private ArrayList<ModelListener> subscribers;

    /**
     * Constructor of the applications model
     */
    public ApplicationModel() {
        this.unfilteredCacheList = new ArrayList<>();
        this.filteredCacheList = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        generateFakeCacheData();
    }

    public void setUnfilteredCacheList(ArrayList<GeoCache> unfilteredCacheList) {
        this.unfilteredCacheList = unfilteredCacheList;
    }

    public void setFilteredCacheList(ArrayList<GeoCache> filteredCacheList) {
        this.filteredCacheList = filteredCacheList;
    }

    public ArrayList<GeoCache> getUnfilteredCacheList() {
        return unfilteredCacheList;
    }

    public ArrayList<GeoCache> getFilteredCacheList() {
        return filteredCacheList;
    }


    public void updateNearbyCacheList(float latitude, float longitude, float distance) {
        // TODO: query database and update nearby cache list based on location and distance
        // clear recommended cache
        this.recommendedCache = null;
        notifySubscribers();
    }

    /**
     * Method to update the filtered cache list based on a list of predicates
     *
     * @param filterList - The list of filters/predicates to filter the cache list by
     */
    public void updateFilteredCacheList(ArrayList<Predicate<GeoCache>> filterList) {
        List<GeoCache> result = this.unfilteredCacheList; // gets the current list of nearby caches
        for (Predicate<GeoCache> filter : filterList) {
            result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter
        }

        // convert filtered caches to arraylist and update
        this.filteredCacheList = (ArrayList<GeoCache>) result;
        notifySubscribers();
    }

    /**
     * Sets a randomly selected nearby cache that meets filter criteria to be recommended
     *
     * @param filterList - the filter criteria to meet
     * @return - Returns a geocache from the nearby cache list that meets criteria, otherwise returns null if no caches meet criteria
     */
    public GeoCache getRecommendedCache(ArrayList<Predicate<GeoCache>> filterList) {
        List<GeoCache> result = this.unfilteredCacheList; // gets the current list of nearby caches
        for (Predicate<GeoCache> filter : filterList) {
            result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter
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

    public GeoCache SearchByID(String cacheID) {
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

    private void generateFakeCacheData() {
        String data = "Geology 101\t52.13175\t-106.63485\t1\t4\t3\t6586501\t5430256\n" +
                "Biology 101\t52.132083\t-106.6344\t1\t3\t1\t6587237\t5430256\n" +
                "Uni campus\t52.134201\t-106.636116\t2\t3\t1\t8678974\t36978910\n" +
                "Fudgy Fest 2012 - Campus Tour\t52.131667\t-106.633333\t1\t2\t1\t3063594\t2126211\n" +
                "Palliser Park - Fudgy Fest 2010\t52.131117\t-106.632667\t2\t2\t1\t1844404\t2126211\n" +
                "Architecture and the Arts\t52.1307\t-106.632983\t4\t2\t1\t7245770\t4944992\n" +
                "Math 101\t52.129917\t-106.63805\t1\t3\t1\t6586541\t5430256\n" +
                "Santa's Pizza\t52.129117\t-106.63545\t2\t2\t1\t1360237\t2126211\n" +
                "FF8 - Rock On!\t52.131567\t-106.6303\t2\t1\t1\t6364037\t2126211\n" +
                "Diefenbaker Legacy - SCAR2010\t52.134233\t-106.640967\t1\t1\t1\t1692991\t556939\n" +
                "Rugby Chapel Redux - SCAR2022\t52.129117\t-106.638083\t4\t4\t1\t8680307\t556939\n" +
                "Sweet Little Cammo\t52.136317\t-106.63355\t2\t1\t1\t2026545\t2126211\n" +
                "Mushroom\t52.136555\t-106.638597\t3\t2\t1\t5741789\t17197545\n" +
                "Marvin Monroe\t52.130383\t-106.6426\t4\t2\t1\t7214306\t12977393\n" +
                "CacheU...Gesundheit\t52.133333\t-106.643367\t1\t3\t2\t5092\t1670040\n" +
                "100% Camo\t52.137558\t-106.635769\t3\t2\t1\t5741771\t17197545\n" +
                "FFV - Social Studies #2\t52.13365\t-106.627333\t4\t1\t1\t4498935\t2126211\n" +
                "Dr. Hibbert\t52.129417\t-106.643133\t4\t5\t1\t6762941\t12977393\n" +
                "Beam Me Up, Scotty!\t52.13765\t-106.631967\t3\t1\t2\t1149207\t70964\n" +
                "Picture This, Saskatoon!\t52.127617\t-106.63115\t1\t3\t2\t7017144\t5435597\n" +
                "StoneCutters\t52.137783\t-106.640083\t4\t2\t1\t7215248\t12977393\n" +
                "I have to park where?!!!!\t52.135483\t-106.627233\t1\t2\t2\t1459391\t2525741\n" +
                "School for the Deaf - SCAR2010\t52.125833\t-106.634267\t2\t1\t1\t1657355\t2126211\n" +
                "FFV - Mohyla Institute\t52.1257\t-106.639083\t4\t2\t1\t4515053\t2126211\n" +
                "FFV - World Traveller #1 - Challenge\t52.135\t-106.62485\t2\t1\t1\t4498944\t2126211\n" +
                "PUZZLING about PRES. MURRAY and the Park-SCAR2019\t52.132833\t-106.647617\t1\t2\t1\t7215748\t436676\n" +
                "Rosemary!\t52.127817\t-106.626583\t1\t2\t2\t6526019\t11568812\n" +
                "Waste Not, Want Not\t52.139633\t-106.6325\t3\t1\t1\t1370698\t2126211\n" +
                "The Dog's Bone..us\t52.1389\t-106.642583\t4\t3\t2\t7809836\t4944992\n";

        String[] lines = data.split("\\n");
        for (String line : lines) {
            String[] lineData = line.split("\\t");

            GeoCache newCache = new GeoCache(lineData[0], Integer.parseInt(lineData[3]), Integer.parseInt(lineData[4]), Integer.parseInt(lineData[5]), lineData[7], Float.parseFloat(lineData[1]), Float.parseFloat(lineData[2]));

            unfilteredCacheList.add(newCache);
        }

        updateFilteredCacheList(new ArrayList<Predicate<GeoCache>>());

    }

    public void init() {
        notifySubscribers();
    }

    public void addNewCache(GeoCache newCache) {
        // TODO: Add cache to DB
        // for now will just add it to the unfiltered cache list, and re-run filters
        this.unfilteredCacheList.add(newCache);
        this.updateFilteredCacheList(new ArrayList<>());
    }
}
