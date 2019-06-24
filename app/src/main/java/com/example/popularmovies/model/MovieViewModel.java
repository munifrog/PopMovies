package com.example.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.popularmovies.utils.HttpManipulator;
import com.example.popularmovies.extractors.MovieDiscoverer;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.popularmovies.MovieConst.ENUM_STATE_FAVORITE;
import static com.example.popularmovies.MovieConst.ENUM_STATE_POPULAR;
import static com.example.popularmovies.MovieConst.ENUM_STATE_RATING;
import static com.example.popularmovies.MovieConst.SETTINGS_FILE;
import static com.example.popularmovies.MovieConst.SETTINGS_SORT_LAST;

public class MovieViewModel extends AndroidViewModel implements
        MovieDiscoverer.MovieDiscoveredListener {
    private int mState;
    private DatabaseContainer mFavorites;
    private DatabaseContainer mPopular;
    private DatabaseContainer mRatings;
    private MoviesChangedListener mListener;

    public MovieViewModel(@NonNull Application application, @Nullable MoviesChangedListener listener) {
        super(application);

        mListener = listener;

        mFavorites = new DatabaseContainer(application, ENUM_STATE_FAVORITE);
        mPopular = new DatabaseContainer(application, ENUM_STATE_POPULAR);
        mRatings = new DatabaseContainer(application, ENUM_STATE_RATING);

        loadMoviesByState(ENUM_STATE_FAVORITE);
        loadMoviesByState(ENUM_STATE_POPULAR);
        loadMoviesByState(ENUM_STATE_RATING);

        SharedPreferences preferences = application.getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        mState = preferences.getInt(SETTINGS_SORT_LAST, ENUM_STATE_POPULAR);

        performNewSearch(mState);
    }

    public interface MoviesChangedListener {
        void onMoviesChanged();
        void onInternetFailure();
    }

    public void performNewSearch(int searchState) {
        if (searchState == ENUM_STATE_POPULAR || searchState == ENUM_STATE_RATING) {
            MovieDiscoverer discoverer = new MovieDiscoverer(this, searchState, this);
            Uri uri = HttpManipulator.getSortedUri(searchState, 1);
            discoverer.execute(uri);
        }
    }

    @Override
    public void onMovieExtractionComplete(LiveData<List<Movie>> movies, int state) {
        mState = state;
        switch(mState) {
            case ENUM_STATE_FAVORITE:
                mFavorites.setMovies(movies);
                if (mListener != null) {
                    mListener.onMoviesChanged();
                }
                break;
            case ENUM_STATE_POPULAR:
                mPopular.setMovies(movies);
                if (mListener != null) {
                    mListener.onMoviesChanged();
                }
                break;
            case ENUM_STATE_RATING:
                mRatings.setMovies(movies);
                if (mListener != null) {
                    mListener.onMoviesChanged();
                }
                break;
        }
    }

    @Override
    public void onInternetFailure() {
        if(mListener != null) {
            mListener.onInternetFailure();
        }
    }

    private void loadMoviesByState(int state) {
        switch(state) {
            case ENUM_STATE_FAVORITE:
                mFavorites.load();
                break;
            case ENUM_STATE_POPULAR:
                mPopular.load();
                break;
            case ENUM_STATE_RATING:
                mRatings.load();
                break;
        }
    }

    public LiveData<List<Movie>> getLiveMovies() {
        return getLiveMovies(mState);
    }

    public LiveData<List<Movie>> getLiveMovies(int state) {
        // It is assumed that the LiveData will change in the future
        switch(state) {
            case ENUM_STATE_FAVORITE:
                return mFavorites.getLiveMovies();
            default:
            case ENUM_STATE_POPULAR:
                return mPopular.getLiveMovies();
            case ENUM_STATE_RATING:
                return mRatings.getLiveMovies();
        }
    }

    public List<Movie> getMovies() {
        switch(mState) {
            case ENUM_STATE_FAVORITE:
                return mFavorites.getMovies();
            default:
            case ENUM_STATE_POPULAR:
                return mPopular.getMovies();
            case ENUM_STATE_RATING:
                return mRatings.getMovies();
        }
    }

    public LocalDatabase getDatabase() {
        return getDatabase(mState);
    }

    public LocalDatabase getDatabase(int state) {
        switch(state) {
            case ENUM_STATE_FAVORITE:
                return mFavorites.getDatabase();
            default:
            case ENUM_STATE_POPULAR:
                return mPopular.getDatabase();
            case ENUM_STATE_RATING:
                return mRatings.getDatabase();
        }
    }

    public int getState() { return mState; }
}
