package com.example.popularmovies.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import static com.example.popularmovies.MovieConst.ENUM_STATE_FAVORITE;
import static com.example.popularmovies.MovieConst.ENUM_STATE_POPULAR;
import static com.example.popularmovies.MovieConst.ENUM_STATE_RATING;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(CalendarConverter.class)
public abstract class LocalDatabase extends RoomDatabase {
    // Modeled after ToyApp:9b

    private static final Object LOCK_FAVES = new Object();
    private static final Object LOCK_POPS = new Object();
    private static final Object LOCK_HIGHS = new Object();
    private static final String DB_NAME_FAVORITES    = "local_db_faves";
    private static final String DB_NAME_POPULAR      = "local_db_popular";
    private static final String DB_NAME_HIGHLY_RATED = "local_db_higher";
    private static LocalDatabase sInstanceFavorites;
    private static LocalDatabase sInstancePopular;
    private static LocalDatabase sInstanceHighlyRated;

    static LocalDatabase getInstance(Context context, int type) {
        switch(type) {
            case ENUM_STATE_FAVORITE:
                if (sInstanceFavorites == null) {
                    synchronized (LOCK_FAVES) {
                        sInstanceFavorites = Room.databaseBuilder(
                                context,
                                LocalDatabase.class,
                                DB_NAME_FAVORITES
                        )
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
                return sInstanceFavorites;
            default:
            case ENUM_STATE_POPULAR:
                if (sInstancePopular == null) {
                    synchronized (LOCK_POPS) {
                        sInstancePopular = Room.databaseBuilder(
                                context,
                                LocalDatabase.class,
                                DB_NAME_POPULAR
                        )
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
                return sInstancePopular;
            case ENUM_STATE_RATING:
                if (sInstanceHighlyRated == null) {
                    synchronized (LOCK_HIGHS) {
                        sInstanceHighlyRated = Room.databaseBuilder(
                                context,
                                LocalDatabase.class,
                                DB_NAME_HIGHLY_RATED
                        )
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
                return sInstanceHighlyRated;
        }
    }

    public abstract DataAccessObject dao();
}
