package com.cmpt370_geocacheapp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import android.content.Context;

import androidx.room.*;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

import com.cmpt370_geocacheapp.database.*;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private GeocacheDao dao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = db.geocacheDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    /* demonstrate that a geocache can be added */
    @Test
    public void writeUserAndReadInList() {
        Geocache c = new Geocache();
        c.latitude = 2F;
        c.longitude = 3F;
        dao.insertAll(c);
        List<Geocache> caches = dao.getAll();
        assertThat(caches.size(), equalTo(1));
        assertThat(caches.get(0).longitude, equalTo(3F));
    }
}

