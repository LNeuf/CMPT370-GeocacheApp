package com.cmpt370_geocacheapp;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ApplicationModelTest extends TestCase {

    public void testGetFilteredCacheList() {
        ApplicationModel testModel = new ApplicationModel();
        assertEquals(testModel.getFilteredCacheList().size(), 29);
        Predicate<PhysicalCacheObject> diffLessThanThree = cacheObject -> (cacheObject.getCacheDifficulty() < 3);
        ArrayList<Predicate<PhysicalCacheObject>> filterList = new ArrayList<>();
        filterList.add(diffLessThanThree);
        testModel.updateFilteredCacheList(filterList); // filtered cache length should be 17 caches with difficulty less than 3
        assertEquals(testModel.getFilteredCacheList().size(), 17);
    }

    public void testUpdateNearbyCacheList() {
        assertTrue(false);
    }

    public void testUpdateFilteredCacheList() {
        ApplicationModel testModel = new ApplicationModel();
        Predicate<PhysicalCacheObject> diffLessThanThree = cacheObject -> (cacheObject.getCacheDifficulty() < 3);
        ArrayList<Predicate<PhysicalCacheObject>> filterList = new ArrayList<>();
        filterList.add(diffLessThanThree);
        testModel.updateFilteredCacheList(filterList);
        boolean filterWorked = true;
        for (PhysicalCacheObject cache : testModel.getFilteredCacheList())
        {
            if (cache.getCacheDifficulty() > 2) {
                filterWorked = false;
                break;
            }
        }
        assert(filterWorked);
    }

    public void testUpdateFilteredCacheListMultipleFilters() {
        ApplicationModel testModel = new ApplicationModel();
        Predicate<PhysicalCacheObject> diffEqualsTwo = cacheObject -> (cacheObject.getCacheDifficulty() == 2);
        Predicate<PhysicalCacheObject> cacheSizeMicro = cacheObject -> (cacheObject.getCacheSize() == 1);
        ArrayList<Predicate<PhysicalCacheObject>> filterList = new ArrayList<>();
        filterList.add(diffEqualsTwo);
        filterList.add(cacheSizeMicro);
        testModel.updateFilteredCacheList(filterList);
        boolean filterWorked = true;
        for (PhysicalCacheObject cache : testModel.getFilteredCacheList())
        {
            if (cache.getCacheDifficulty() > 2 || cache.getCacheSize() != 1) {
                filterWorked = false;
                break;
            }
        }
        assert(filterWorked);
    }

    public void testGetRecommendedCache() {
        ApplicationModel testModel = new ApplicationModel();
        Predicate<PhysicalCacheObject> diffEqualsTwo = cacheObject -> (cacheObject.getCacheDifficulty() == 2);
        Predicate<PhysicalCacheObject> cacheSizeMicro = cacheObject -> (cacheObject.getCacheSize() == 1);
        ArrayList<Predicate<PhysicalCacheObject>> filterList = new ArrayList<>();
        filterList.add(diffEqualsTwo);
        filterList.add(cacheSizeMicro);
        PhysicalCacheObject recommendedCache = testModel.getRecommendedCache(filterList,null, 0); //TODO: fix this test
        assertEquals(recommendedCache.getCacheDifficulty(), 2);
        assertEquals(recommendedCache.getCacheSize(), 1);
    }

    public void testSearchByID() {
        assertTrue(false);
    }


}