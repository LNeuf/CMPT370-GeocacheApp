import javax.xml.stream.events.Comment;
import java.util.ArrayList;

public class PhysicalCacheObject {
    CacheObject cache;
    private final double latitude;
    private final double longitude;
    private final String difficulty;
    private final String terrain;



    public PhysicalCacheObject(CacheObject cache, double latitude, double longitude, String difficulty,  String terrain) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.difficulty = difficulty;
        this.terrain = terrain;
        this.cache = cache;

    }

    public String getCacheName() {
        return this.cache.getName();
    }
    public String getCacheAuthor() {
        return this.cache.getAuthor();
    }

    public double getCacheLatitude() {
        return this.latitude;
    }

    public double getCacheLongitude() {
        return this.longitude;
    }

    public double[] getCacheCoordinates() {
        /*
        Returns coordinates in the form of latitude and longitude
         */
        double[] cacheCoordinates = new double[2];
        cacheCoordinates[0] = this.latitude;
        cacheCoordinates[1] = this.longitude;
        return cacheCoordinates;
    }
    public String getCacheDifficulty() {
        return this.difficulty;
    }

    public long getCacheID() {
        return this.cache.getCacheID();
    }

    public String getTerrain() {
        return this.terrain;
    }

    public void addComment(CacheComment commentToAdd) {
        cache.addComment(commentToAdd);
    }

    public CacheComment readComment(long commentID) {
        int indx = -1;
        for (int i = 0; i < this.cache.getCommentList().size(); i++) {
            if (this.cache.getCommentList().get(i).getCommentID() == commentID) {
                indx = i;
            }
        }
        if (indx != -1) {
            return this.cache.getCommentList().get(indx);
        }
        return null;
    }



    public String getCreationDate() {
        return this.cache.getCreationDate();
    }

    public String getDateLastAccessed() {
        return this.cache.getDateLastAccessed();
    }

    public static void main(String[] args) {
        int testsPerformed = 0;
        int expectedTests = 0;

        String expectedName = "Saskatoon cache";
        String expectedAuthor = "Ashton";
        double expectedLatitude = 18.329384;
        double expectedLongitude = 14.00343;
        String expectedDifficulty = "Medium";
        long expectedCacheID = 1;
        String expectedTerrain = "Plains";

        CacheObject cache = new CacheObject("Saskatoon cache", "Ashton",1);
        PhysicalCacheObject physicalCache = new PhysicalCacheObject(cache, 18.329384,14.00343,"Medium","Plains");


        testsPerformed += 1;
        expectedTests += 1;
        if (!expectedName.equals(physicalCache.getCacheName())) {
            testsPerformed -= 1;
            System.out.println("getCacheName() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (!expectedAuthor.equals(physicalCache.getCacheAuthor())) {
            testsPerformed -= 1;
            System.out.println("getCacheAuthor() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedLatitude != physicalCache.getCacheLatitude()) {
            testsPerformed -= 1;
            System.out.println("getCacheLatitude() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedLongitude != physicalCache.getCacheLongitude()) {
            testsPerformed -= 1;
            System.out.println("getCacheLongitude() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (!expectedDifficulty.equals(physicalCache.getCacheDifficulty())) {
            testsPerformed -= 1;
            System.out.println("getCacheDifficulty() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedCacheID != cache.getCacheID()) {
            testsPerformed -= 1;
            System.out.println("getCacheID() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (!expectedTerrain.equals(physicalCache.getTerrain())) {
            testsPerformed -= 1;
            System.out.println("getTerrain() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (physicalCache.getCacheLatitude() != physicalCache.getCacheCoordinates()[0]) {
            testsPerformed -= 1;
            System.out.println("getCacheCoordinates() or getCacheLatitude() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (physicalCache.getCacheLongitude() != physicalCache.getCacheCoordinates()[1]) {
            testsPerformed -= 1;
            System.out.println("getCacheCoordinates() or getCacheLongitude() method failed.");
        }
        User author = new User("Ashton", "potato", 1);
        CacheComment comment = new CacheComment("Comment body test for a series of characters.", author, 1);
        cache.addComment(comment);

        // TODO: Add testing for comment list with comment object and review object


        System.out.println(testsPerformed + " tests were performed out of " + expectedTests);
    }
}
