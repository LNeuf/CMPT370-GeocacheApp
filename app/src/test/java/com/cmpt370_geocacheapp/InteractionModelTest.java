package com.cmpt370_geocacheapp;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.function.Predicate;

public class InteractionModelTest extends TestCase {

    public void testGetAndSetCurrentlySelectedCache() {
        InteractionModel testIModel = new InteractionModel();
        PhysicalCacheObject testCache = new PhysicalCacheObject(new CacheObject("TestName", "TestAuthor", 123),50,80,1,2,3 );
        testIModel.setCurrentlySelectedCache(testCache);
        assertEquals(testCache, testIModel.getCurrentlySelectedCache());
    }


    public void testClearFilters() {
        InteractionModel testIModel = new InteractionModel();
        ArrayList<Predicate<PhysicalCacheObject>> testFilters = new ArrayList<>();
        Predicate<PhysicalCacheObject> diffLessThanThree = cacheObject -> (cacheObject.getCacheDifficulty() < 3);
        testFilters.add(diffLessThanThree);
        assertEquals(0, testIModel.getCurrentFilters().size());
        testIModel.updateFilters(testFilters);
        assertEquals(1, testIModel.getCurrentFilters().size());
    }

    public void testGetCurrentFilters() {
        InteractionModel testIModel = new InteractionModel();
        assertEquals(0,testIModel.getCurrentFilters().size()); //  no filters to start
        ArrayList<Predicate<PhysicalCacheObject>> testFilters = new ArrayList<>();
        Predicate<PhysicalCacheObject> diffLessThanThree = cacheObject -> (cacheObject.getCacheDifficulty() < 3);
        testFilters.add(diffLessThanThree);
        testIModel.updateFilters(testFilters);
        assertEquals(1, testIModel.getCurrentFilters().size());
        assertEquals(diffLessThanThree, testIModel.getCurrentFilters().get(0));
    }

}