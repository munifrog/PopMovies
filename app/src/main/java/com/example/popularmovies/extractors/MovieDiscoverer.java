package com.example.popularmovies.extractors;

import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.popularmovies.model.LocalDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieViewModel;
import com.example.popularmovies.utils.HttpManipulator;
import com.example.popularmovies.utils.JsonManipulator;

import java.util.List;

// COMPLETED: Consider extracting this AsyncTask implementation into a separate class file in order
//  to improve code readability and maintainability. (I would have to communicate transitionSort
//  and Movie array)
// URL <= Input params; Void <= progress; List<Movie> <= result
public class MovieDiscoverer extends AsyncTask<Uri, Void, LiveData<List<Movie>>> {
    private MovieViewModel mViewModel;
    private int mState;
    private MovieDiscoveredListener mListener;

    public MovieDiscoverer(
            MovieViewModel viewModel,
            int state,
            MovieDiscoveredListener listener
    ) {
        mViewModel = viewModel;
        mState = state;
        mListener = listener;
    }

    public interface MovieDiscoveredListener {
        void onMovieExtractionComplete(LiveData<List<Movie>> movies, int state);
        void onInternetFailure();
    }

    @Override
    protected LiveData<List<Movie>> doInBackground(Uri... uris) {
        LocalDatabase db = mViewModel.getDatabase(mState);
        try {
            Uri uri = uris[0];
            String json = HttpManipulator.getResponse(HttpManipulator.uri2url(uri));
            List<Movie> parsedMovies = JsonManipulator.extractMoviesFromJson(json);
            List<Movie> oldMovies = db.dao().loadAllImmediately();
            if(oldMovies != null) {
                db.dao().deleteMovies(oldMovies);
            }
            db.dao().insertMovies(parsedMovies);
            db.dao().loadAllImmediately(); // forces the movie retrieval to finish
        } catch (RuntimeException e) {
            mListener.onInternetFailure();
        }
        return db.dao().loadAll();
    }

    @Override
    protected void onPostExecute(LiveData<List<Movie>> newMovies) {
        super.onPostExecute(newMovies);
        mListener.onMovieExtractionComplete(newMovies, mState);
    }
}