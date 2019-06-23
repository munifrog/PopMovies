package com.example.popularmovies.utils;

import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.popularmovies.model.LocalDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieViewModel;

import java.util.List;

// COMPLETED: Consider extracting this AsyncTask implementation into a separate class file in order
//  to improve code readability and maintainability. (I would have to communicate transitionSort
//  and Movie array)
// URL <= Input params; Void <= progress; List<Movie> <= result
public class SortedMovieDiscoverer extends AsyncTask<Uri, Void, LiveData<List<Movie>>> {
    private MovieViewModel mViewModel;
    private int mState;

    public SortedMovieDiscoverer(MovieViewModel viewModel, int state) {
        mViewModel = viewModel;
        mState = state;
    }

    @Override
    protected LiveData<List<Movie>> doInBackground(Uri... uris) {
        LiveData<List<Movie>> newMovies = null;
        try {
            Uri uri = uris[0];
            String json = HttpManipulator.getResponse(HttpManipulator.uri2url(uri));
            List<Movie> parsedMovies = JsonManipulator.extractMoviesFromJson(json);
            LocalDatabase db = mViewModel.getDatabase();
            List<Movie> oldMovies = db.dao().loadAllImmediately();
            if(oldMovies != null) {
                db.dao().deleteMovies(oldMovies);
            }
            db.dao().insertMovies(parsedMovies);
            db.dao().loadAllImmediately(); // forces it to finish loading first
            newMovies = db.dao().loadAll();
        } catch (RuntimeException e) {
            // Do nothing; use previously stored results
            // Internet unavailable
        }
        return newMovies;
    }

    @Override
    protected void onPostExecute(LiveData<List<Movie>> newMovies) {
        super.onPostExecute(newMovies);
        mViewModel.updateMovies(newMovies, mState);
    }
}