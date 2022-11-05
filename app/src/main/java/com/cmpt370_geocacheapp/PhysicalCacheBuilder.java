public class PhysicalCacheBuilder implements CacheBuilder {
    private CacheObject cache;

    final private CacheDatabase database;

    public PhysicalCacheBuilder(CacheDatabase databaseToLink) {
        this.database = databaseToLink;
    }

    public void buildCache(String name, String author, long cacheID) {
         this.cache = new CacheObject(name, author, cacheID);
    }
    public void buildPhysicalCache(double latitude, double longitude, String difficulty, String terrain) {
        this.database.addPhysicalCache(new PhysicalCacheObject(this.cache, latitude, longitude, difficulty,terrain));
    }

    public static void main(String[] args) {
        //TODO: make tests for physicalCacheBuilder
    }
}
