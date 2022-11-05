import java.util.ArrayList;

public class CacheDatabase {
    final private ArrayList<PhysicalCacheObject> physicalCacheList;

    public CacheDatabase() {
        this.physicalCacheList = new ArrayList<>();
    }

    public ArrayList<PhysicalCacheObject> getPhysicalCacheList() {
        return this.physicalCacheList;
    }

    public PhysicalCacheObject getPhysicalCache(long cacheID) {
        int indx = -1;
        for (int i = 0; i < physicalCacheList.size(); i++) {
            if (physicalCacheList.get(i).getCacheID() == cacheID) {
                indx = i;
            }
        }
        if (indx != -1) {
            return physicalCacheList.get(indx);
        }
        return null;
    }


    public void addPhysicalCache(PhysicalCacheObject newCache) {
        physicalCacheList.add(newCache);
    }

    public void removePhysicalCache(PhysicalCacheObject cache) {
        physicalCacheList.remove(cache);
    }

    public void removeCacheObject(long cacheID) {
        int indx = -1;
        for (int i = 0; i < physicalCacheList.size(); i++) {
            if (physicalCacheList.get(i).getCacheID() == cacheID) {
                indx = i;
            }
        }
        if (indx != -1) {
            physicalCacheList.remove(indx);
        }
    }
    private double squared(double num) {
        return num * num;
    }

    public ArrayList<PhysicalCacheObject> filterCacheDistance(double maxDistance, double currentLatitude, double currentLongitude) {
        ArrayList<PhysicalCacheObject> distanceFilteredCaches = new ArrayList<>();
        double lat;
        double lon;
        double distanceSquared;
        for (PhysicalCacheObject physicalCache : physicalCacheList) {
            lat = physicalCache.getCacheLatitude();
            lon = physicalCache.getCacheLongitude();

            distanceSquared = squared(lat - currentLatitude) + squared(lon - currentLongitude);

            if (distanceSquared <= squared(maxDistance)) {
                distanceFilteredCaches.add(physicalCache);
            }

        }
        return distanceFilteredCaches;
    }
    public ArrayList<CacheObject> filterTerrain(String allowedTerrain) {

        return null;
    }

    public ArrayList<CacheObject> filterAuthor (String authorName) {
        return null;
    }

    public ArrayList<CacheObject> filterDifficulty(String allowedDifficulty) {
        return null;
    }


    public static void main(String[] args) {
        //TODO: Make tests for database
    }
}
