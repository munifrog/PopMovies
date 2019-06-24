package com.example.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApp;
    private MovieViewModel.MoviesChangedListener mListener;

    public MovieViewModelFactory(
            @NonNull Application application,
            @Nullable MovieViewModel.MoviesChangedListener listener
    ) {
        mApp = application;
        mListener = listener;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieViewModel(mApp, mListener);
    }
}
