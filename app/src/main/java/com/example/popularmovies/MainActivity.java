package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.HttpManipulator;
import com.example.popularmovies.utils.JsonManipulator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieConst,
        GridAdapter.GridClickListener {
    private static List<Movie> mMovies;

    private static int mSortState;
    private static boolean mTransitioningSort;
    private static android.support.v7.widget.Toolbar mToolbar;
    private static ProgressBar mProgressBar;
    private static MenuItem mPopItem;
    private static MenuItem mRateItem;

    private static int mOrientation;
    private static RecyclerView mGridRecyclerView;
    private static Context mContext;
    private static GridAdapter.GridClickListener mListener;
    private static int mStatusBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTransitioningSort = false;

        // Alternate Toolbar demonstrated at
        // https://stackoverflow.com/questions/35648913/how-to-set-menu-to-toolbar-in-android
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.main);
        Menu menu = mToolbar.getMenu();
        mPopItem = menu.findItem(R.id.action_sort_popularity);
        mRateItem = menu.findItem(R.id.action_sort_rating);
        showSortByMenuItem();
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int action = item.getItemId();
                switch(action) {
                    case R.id.action_about:
                        showAboutInfo();
                        return true;
                    case R.id.action_sort_popularity:
                        alternateMenuState();
                        sortByCurrentChoice();
                        return true;
                    case R.id.action_sort_rating:
                        alternateMenuState();
                        sortByCurrentChoice();
                        return true;
                }

                return false;
            }
        });

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        restoreState();
        sortByCurrentChoice();

        // Orientation detection described here:
        // https://stackoverflow.com/questions/2795833/check-orientation-on-android-phone
        mOrientation = getResources().getConfiguration().orientation;
        mGridRecyclerView = findViewById(R.id.rv_grid);
        mContext = this;
        mListener = this;

        mStatusBarHeight = getStatusBarHeight();
    }

    static void postMovieRetrieval() {
        if(mMovies != null) {
            int length = mMovies.size();
            String [] images = new String[length];
            String currentImageUrl;
            for(int i = 0; i < length; i++) {
                Movie movie = mMovies.get(i);

                currentImageUrl = movie.getImageUrl();
                if(currentImageUrl != null && !currentImageUrl.equals("null")) {
                    images[i] = currentImageUrl;
                } else {
                    images[i] = "";
                }
            }

            int spanCount = (mOrientation == Configuration.ORIENTATION_LANDSCAPE ?
                    PREF_SPAN_LANDSCAPE : PREF_SPAN_PORTRAIT);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, spanCount);
            mGridRecyclerView.setLayoutManager(layoutManager);
            GridAdapter adapter = new GridAdapter(images, mListener, ENUM_IMAGE_0342);
            mGridRecyclerView.setAdapter(adapter);

            mTransitioningSort = false;
            mProgressBar.setVisibility(View.INVISIBLE);

            mGridRecyclerView.setPadding(0, mStatusBarHeight,0,0);
        } else {
            Toast.makeText(mContext, R.string.error_internet_failure, Toast.LENGTH_LONG).show();
        }
    }

    // https://stackoverflow.com/questions/20584325/reliably-get-height-of-status-bar-to-solve-kitkat-translucent-navigation-issue
    private int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
        // Try to avoid warned memory leak
        mToolbar = null;
        mProgressBar = null;
    }

    // URL <= Input params; Void <= progress; List<Movie> <= result
    static class SortedMovieDiscoverer extends AsyncTask<Uri, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Uri... uris) {
            List<Movie> movies = null;
            try {
                Uri uri = uris[0];
                String json = HttpManipulator.getResponse(HttpManipulator.uri2url(uri));
                movies = JsonManipulator.extractMoviesFromJson(json);
            } catch (RuntimeException e) {
                // Internet unavailable
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mMovies = movies;
            postMovieRetrieval();
        }
    }

    private void sortByPopularity() {
        // Should only be called by sortByCurrentChoice()
        Uri uri = HttpManipulator.getSortedUri(0, 1);
        new SortedMovieDiscoverer().execute(uri);
    }

    private void sortByRatings() {
        // Should only be called by sortByCurrentChoice()
        Uri uri = HttpManipulator.getSortedUri(1, 1);
        new SortedMovieDiscoverer().execute(uri);
    }

    private void showAboutInfo() {
        Toast.makeText(this, R.string.tmdb_attribution, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(int position) {
        launchDetails(position);
    }

    private void launchDetails(int index) {
        if(mMovies != null && index > -1 && index < MAX_MOVIES_RETRIEVED) {
            Movie movie = mMovies.get(index);
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(ENTIRE_PARCELLED_MOVIE, movie);
            startActivity(intent);
        }
    }

    private void alternateMenuState() {
        if (!mTransitioningSort) {
            if (mSortState == ENUM_SORT_AVERAGE_RATING_DESCENDING) {
                mSortState = ENUM_SORT_POPULARITY_DESCENDING;
                showSortByMenuItem();
            } else if (mSortState == ENUM_SORT_POPULARITY_DESCENDING) {
                mSortState = ENUM_SORT_AVERAGE_RATING_DESCENDING;
                showSortByMenuItem();
            }

        }
    }

    private static void showSortByMenuItem() {
        if (mSortState == ENUM_SORT_AVERAGE_RATING_DESCENDING) {
            mToolbar.setTitle(R.string.sort_ctx_rating);
            mPopItem.setVisible(true);
            mRateItem.setVisible(false);
        } else if (mSortState == ENUM_SORT_POPULARITY_DESCENDING) {
            mToolbar.setTitle(R.string.sort_ctx_popular);
            mPopItem.setVisible(false);
            mRateItem.setVisible(true);
        }
    }

    private void sortByCurrentChoice() {
        if (!mTransitioningSort) {
            mTransitioningSort = true;
            mProgressBar.setVisibility(View.VISIBLE);
            if (mSortState == ENUM_SORT_AVERAGE_RATING_DESCENDING) {
                sortByRatings();
            } else if (mSortState == ENUM_SORT_POPULARITY_DESCENDING) {
                sortByPopularity();
            }
        }
    }

    // I've done this before but I usually forget the details; Refreshed my memory with this site:
    // https://stackoverflow.com/questions/10209814/saving-user-information-in-app-settings
    private void saveState() {
        SharedPreferences preferences = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SETTINGS_SORT_LAST, mSortState);
        editor.apply();
    }

    private void restoreState() {
        SharedPreferences preferences = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        mSortState = preferences.getInt(SETTINGS_SORT_LAST, ENUM_SORT_POPULARITY_DESCENDING);
    }
}
