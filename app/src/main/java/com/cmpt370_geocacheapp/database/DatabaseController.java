package com.cmpt370_geocacheapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.cmpt370_geocacheapp.GeoCache;

import java.util.Locale;
//import DatabaseContract.*;

/* A programmer interface to the database. abstracts away dealing with SQL operations
 */
public class DatabaseController {
    Context context;
    private DbHelper dbHelper;
    SQLiteDatabase db;

    public DatabaseController(Context context) {
        this.context = context;
    }

    public void open() throws SQLiteException {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    // TODO (for debugging demonstration, doesnt reutrn stuff yet)
    public void fetchByDistance(float radius, float longitude, float latitude) {
        float radiusSqr = radius * radius;
//        String[] selectionArgs = { ""+longitude, ""+longitude, ""+latitude, ""+latitude, ""+radiusSqr };
        Cursor cursor = db.query(
                "geocaches",
                null,
                String.format(
                        Locale.CANADA,
                        "(((longitude - %f) * (longitude - %f)) + ((latitude - %f) * (latitude - %f))) <= %f",
                        longitude, longitude, latitude, latitude, radiusSqr
                ),
//                selectionArgs,
                null,
                null,
                null,
                null,
                null
        );
        String s;
        while (cursor.moveToNext()) {
            s = cursor.getString(2);
            System.out.println(s);
        }
        cursor.close();
    }

    public void addGeocache(String author, float longitude, float latitude) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(GeocacheDef.AUTHOR, author);
        contentValue.put(GeocacheDef.LONGITUDE, longitude);
        contentValue.put(GeocacheDef.LATITUDE, latitude);
        db.insert(GeocacheDef.TABLE_NAME, null, contentValue);
    }
}
