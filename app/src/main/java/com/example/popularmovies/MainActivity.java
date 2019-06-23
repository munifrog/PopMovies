package com.example.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popularmovies.model.CalendarConverter;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieConst,
        GridAdapter.GridClickListener {
    private static boolean mTransitioningSort;
    private static MenuItem mFaveItem;
    private static MenuItem mPopItem;
    private static MenuItem mRateItem;
    private static GridAdapter mAdapter;
    private static MovieViewModel mViewModel;

    private static final String INDICATE_STATUS_BAR = "status_bar_height";
    private static final String INDICATE_DIMENSION  = "dimen";
    private static final String INDICATE_PACKAGE    = "android";
    private static final String STRING_NOMINALLY_NULL = "null";
    private static final String STRING_EMPTY        = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTransitioningSort = true;
        setupViewModel();

        // Alternate Toolbar demonstrated at
        // https://stackoverflow.com/questions/35648913/how-to-set-menu-to-toolbar-in-android
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        Menu menu = toolbar.getMenu();
        mFaveItem = menu.findItem(R.id.action_sort_favorites);
        mPopItem = menu.findItem(R.id.action_sort_popularity);
        mRateItem = menu.findItem(R.id.action_sort_rating);
        showSortByMenuItem();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int action = item.getItemId();
                switch(action) {
                    case R.id.action_about:
                        showAboutInfo();
                        return true;
                    case R.id.action_sort_favorites:
                        sortByCurrentChoice(ENUM_STATE_FAVORITE);
                        return true;
                    case R.id.action_sort_popularity:
                        sortByCurrentChoice(ENUM_STATE_POPULAR);
                        return true;
                    case R.id.action_sort_rating:
                        sortByCurrentChoice(ENUM_STATE_RATING);
                        return true;
                }

                return false;
            }
        });

        // Orientation detection described here:
        // https://stackoverflow.com/questions/2795833/check-orientation-on-android-phone
        int spanCount = (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE ?
                PREF_SPAN_LANDSCAPE :
                PREF_SPAN_PORTRAIT
        );

        RecyclerView gridRecyclerView = findViewById(R.id.rv_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        gridRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GridAdapter(this, ENUM_IMAGE_0342);
        gridRecyclerView.setAdapter(mAdapter);
        gridRecyclerView.setPadding(0, getStatusBarHeight(),0,0);
    }

    private void updateAdapterWithNewMovieSet() {
        if (mViewModel.getMovies() != null) {
            List<Movie> movies = mViewModel.getMovies();
            if (movies != null) {
                int length = mViewModel.getMovies().size();
                String[] images = new String[length];
                String currentImageUrl;
                for (int i = 0; i < length; i++) {
                    Movie movie = movies.get(i);
                    currentImageUrl = movie.getImagePath();
                    if (currentImageUrl != null && !currentImageUrl.equals(STRING_NOMINALLY_NULL)) {
                        images[i] = currentImageUrl;
                    } else {
                        images[i] = STRING_EMPTY;
                    }
                }
                mAdapter.setMovieImages(images);
            } else {
                Toast.makeText(this, R.string.error_internet_failure, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.error_internet_failure, Toast.LENGTH_LONG).show();
        }
        mTransitioningSort = false;
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        retrieveMovies();
    }

    public void retrieveMovies() {
        mViewModel.getLiveMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(movies != null) {
                    updateAdapterWithNewMovieSet();
                    showWaitingBar();
                }
            }
        });
    }

    // https://stackoverflow.com/questions/20584325/reliably-get-height-of-status-bar-to-solve-kitkat-translucent-navigation-issue
    private int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier(
                INDICATE_STATUS_BAR,
                INDICATE_DIMENSION,
                INDICATE_PACKAGE
        );

        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    private void showAboutInfo() {
        Toast.makeText(this, R.string.tmdb_attribution, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(int position) {
        launchDetails(position);
    }

    private void launchDetails(int index) {
        if(index > -1 && index < MAX_MOVIES_RETRIEVED && mViewModel.getMovies() != null) {
            Movie movie = mViewModel.getMovies().get(index);
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(MOVIE_ID, movie.getId());
            intent.putExtra(MOVIE_TITLE_LOCAL, movie.getTitleCurrent());
            intent.putExtra(MOVIE_TITLE_ORIG, movie.getTitleOriginal());
            intent.putExtra(MOVIE_IMAGE_PATH, movie.getImagePath());
            intent.putExtra(MOVIE_OVERVIEW, movie.getOverview());
            intent.putExtra(MOVIE_RATING, movie.getRating());
            intent.putExtra(MOVIE_RELEASE, CalendarConverter.convertToLong(movie.getRelease()));
            startActivity(intent);
        }
    }

    private void showSortByMenuItem() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        int currentState = mViewModel.getState();
        if (currentState == ENUM_STATE_RATING) {
            toolbar.setTitle(R.string.sort_ctx_rating);
            mFaveItem.setVisible(true);
            mPopItem.setVisible(true);
            mRateItem.setVisible(false); // No need to see self as an option to change to
        } else if (currentState == ENUM_STATE_POPULAR) {
            toolbar.setTitle(R.string.sort_ctx_popular);
            mFaveItem.setVisible(true);
            mPopItem.setVisible(false); // No need to see self as an option to change to
            mRateItem.setVisible(true);
        } else if (currentState == ENUM_STATE_FAVORITE) {
            toolbar.setTitle(R.string.sort_ctx_favorite);
            mFaveItem.setVisible(false); // No need to see self as an option to change to
            mPopItem.setVisible(true);
            mRateItem.setVisible(true);
        }
    }

    private void showWaitingBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(mTransitioningSort ? View.VISIBLE : View.INVISIBLE);
    }

    private void sortByCurrentChoice(int state) {
        if (!mTransitioningSort) {
            mTransitioningSort = true;
            showWaitingBar();
            mViewModel.performNewSearch(state);
        }
    }

    // I've done this before but I usually forget the details; Refreshed my memory with this site:
    // https://stackoverflow.com/questions/10209814/saving-user-information-in-app-settings
    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SETTINGS_SORT_LAST, mViewModel.getState());
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePreferences();
    }
}
