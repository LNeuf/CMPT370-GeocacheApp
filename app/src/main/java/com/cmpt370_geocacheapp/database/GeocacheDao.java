package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dao
public interface GeocacheDao {
    @Insert
    void insertAll(Geocache... caches);

    @Delete
    void deleteAll(Geocache... caches);

    @Update
    void updateAll(Geocache... caches);

    @Query("SELECT * FROM Geocache")
    List<Geocache> getAll();

    // -90 <= latitude <= 90
    // 0 <= longitude <= 360
    // note that this is really specifying a trapezoidal-shaped region
    // cause latitude/longitude is weird
    @Query(
            "SELECT * FROM Geocache WHERE " +
                    ":latitudeMin <= latitude AND " +
                    "latitude <= :latitudeMax AND " +
                    ":longitudeMin <= longitude AND " +
                    "longitude <= :longitudeMax"

    )
    List<Geocache> getByLatLong(double latitudeMin, double latitudeMax,
                                double longitudeMin, double longitudeMax);

    @Query(
            "SELECT * FROM Geocache WHERE " +
                    "id == :geocacheId"

    )
    Geocache getByCacheID(long geocacheId);

    @Query(
            "SELECT * FROM Geocache WHERE userUsername LIKE '%' || :authorText || '%'"

    )
    List<Geocache> getByAuthorString(String authorText);

    @Query(
            "SELECT * FROM Geocache WHERE cacheName LIKE '%' || :cacheNameText || '%'"

    )
    List<Geocache> getByCacheNameString(String cacheNameText);

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN User ON Geocache.id = :geocacheId AND " +
                    "Geocache.userUsername = User.username"
    )
    User getUser(long geocacheId);

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN Comment ON Geocache.id = Comment.geocacheId " +
                    "AND Geocache.id = :geocacheId_"
    )
    List<Comment> getComments(long geocacheId_);

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN RatingReview ON Geocache.id = RatingReview.geocacheId " +
                    "AND Geocache.id = :geocacheId_"
    )
    List<RatingReview> getReviews(long geocacheId_);
}

