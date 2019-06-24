package com.example.popularmovies.extractors;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.popularmovies.model.LocalDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieViewModel;

import java.util.List;

import static com.example.popularmovies.MovieConst.ENUM_STATE_FAVORITE;

public class FavoritesLoader extends AsyncTask<Void, Void, LiveData<List<Movie>>> {
    private MovieViewModel mViewModel;
    private FavoritesLoaderListener mListener;

    public FavoritesLoader(MovieViewModel viewModel, FavoritesLoaderListener listener) {
        mViewModel = viewModel;
        mListener = listener;
    }

    public interface FavoritesLoaderListener {
        void onMovieExtractionComplete(LiveData<List<Movie>> movies, int state);
    }

    @Override
    protected LiveData<List<Movie>> doInBackground(Void... voids) {
        LocalDatabase db = mViewModel.getDatabase(ENUM_STATE_FAVORITE);
        return db.dao().loadAll();
    }

    @Override
    protected void onPostExecute(LiveData<List<Movie>> movies) {
        super.onPostExecute(movies);
        mListener.onMovieExtractionComplete(movies, ENUM_STATE_FAVORITE);
    }
}
