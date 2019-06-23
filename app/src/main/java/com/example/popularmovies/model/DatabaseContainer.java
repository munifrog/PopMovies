package com.example.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

class DatabaseContainer {
    private LiveData<List<Movie>> mMoviesLive;
    private List<Movie> mMovies;
    private LocalDatabase mDatabase;
    private Observer mObserver;

    DatabaseContainer(Application application, int state) {
        mDatabase = LocalDatabase.getInstance(application, state);
        mObserver = new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                removeObserver();
                mMovies = mMoviesLive.getValue();
            }
        };
        load();
    }

    void load() {
        removeObserver();
        mMoviesLive = mDatabase.dao().loadAll();
        mMoviesLive.observeForever(mObserver);
    }

    void setMovies(LiveData<List<Movie>> newMovies) {
        if(newMovies != null) {
            removeObserver();
            mMoviesLive = newMovies;
            mMoviesLive.observeForever(mObserver);
        }
    }

    private void removeObserver() {
        if(mMoviesLive != null) {
            mMoviesLive.removeObserver(mObserver);
        }
    }

    LiveData<List<Movie>> getLiveMovies() { return mMoviesLive; }
    List<Movie> getMovies() { return mMovies; }

    LocalDatabase getDatabase() { return mDatabase; }
}
