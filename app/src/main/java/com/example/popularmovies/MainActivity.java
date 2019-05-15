package com.example.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.HttpManipulator;
import com.example.popularmovies.utils.JsonManipulator;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieConst {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static List<Movie> mMovies;

    private static int mSortState;
    private static Menu mMenu;
    private static MenuItem mPopItem;
    private static MenuItem mRateItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreState();
        sortByCurrentChoice();

        Button details = findViewById(R.id.button_details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetails(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
    }

    // URL <= Input params; Void <= progress; List<Movie> <= result
    static class SortedMovieDiscoverer extends AsyncTask<Uri, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Uri... uris) {
            Uri uri = uris[0];

            String json = HttpManipulator.getResponse(HttpManipulator.uri2url(uri));
            List<Movie> movies = JsonManipulator.extractMoviesFromJson(json);
            if(movies != null) {
                int length = movies.size();
                for(int i = 0; i < length; i++) {
                    Movie movie = movies.get(i);
                    Log.v(TAG, "Sorted[" + i + "]: \"" + movie.getTitle() + "\"");
                    Log.v(TAG, "Sorted[" + i + "]: \"" + HttpManipulator.getImageUri(movie.getImageUrl()) + "\"");
                }
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mMovies = movies;
        }
    }

    private void sortByPopularity() {
        Uri uri = HttpManipulator.getSortedUri(0);
        Log.v(TAG, uri.toString());

        new SortedMovieDiscoverer().execute(uri);
    }

    private void sortByRatings() {
        Uri uri = HttpManipulator.getSortedUri(1);
        Log.v(TAG, uri.toString());

        new SortedMovieDiscoverer().execute(uri);
    }

    private void showAboutInfo() {
        Toast.makeText(this, R.string.tmdb_attribution, Toast.LENGTH_LONG).show();
    }

    private void launchDetails(int index) {
        if(mMovies != null && index > -1 && index < MAX_MOVIES_RETRIEVED) {
            Movie movie = mMovies.get(index);

            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(DETAILS_TITLE, movie.getTitle());
            intent.putExtra(DETAILS_PLOT, movie.getOverview());
            Calendar date = movie.getRelease();
            intent.putExtra(DETAILS_RELEASE_DAY, date.get(Calendar.DATE));
            intent.putExtra(DETAILS_RELEASE_MONTH, date.get(Calendar.MONTH));
            intent.putExtra(DETAILS_RELEASE_YEAR, date.get(Calendar.YEAR));
            intent.putExtra(DETAILS_AVERAGE_RATING, Math.round(100.0f * movie.getRating()));
            intent.putExtra(DETAILS_IMAGE_URL, movie.getImageUrl());

            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        mPopItem = mMenu.findItem(R.id.action_sort_popularity);
        mRateItem = mMenu.findItem(R.id.action_sort_rating);
        showSortByMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int action = item.getItemId();
        switch(action) {
            case R.id.action_about:
                showAboutInfo();
                return true;
            case R.id.action_sort_popularity:
                alternateMenuState();
                sortByPopularity();
                return true;
            case R.id.action_sort_rating:
                alternateMenuState();
                sortByRatings();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alternateMenuState() {
        if (mSortState == ENUM_SORT_AVERAGE_RATING_DESCENDING) {
            mSortState = ENUM_SORT_POPULARITY_DESCENDING;
            showSortByMenuItem();
        } else if (mSortState == ENUM_SORT_POPULARITY_DESCENDING) {
            mSortState = ENUM_SORT_AVERAGE_RATING_DESCENDING;
            showSortByMenuItem();
        }
    }

    private void showSortByMenuItem() {
        if (mSortState == ENUM_SORT_AVERAGE_RATING_DESCENDING) {
            mPopItem.setVisible(true);
            mRateItem.setVisible(false);
        } else if (mSortState == ENUM_SORT_POPULARITY_DESCENDING) {
            mPopItem.setVisible(false);
            mRateItem.setVisible(true);
        }
    }

    private void sortByCurrentChoice() {
        if (mSortState == ENUM_SORT_AVERAGE_RATING_DESCENDING) {
            sortByRatings();
        } else if (mSortState == ENUM_SORT_POPULARITY_DESCENDING) {
            sortByPopularity();
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
