package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private static String [] mMonths;
    private static List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMonths = getResources().getStringArray(R.array.months);

        Button popularity = findViewById(R.id.button_popularity);
        popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPopularity();
            }
        });

        Button ratings = findViewById(R.id.button_ratings);
        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByRatings();
            }
        });

        Button details = findViewById(R.id.button_details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetails(0);
            }
        });
    }

    // TODO: Create Grid Layout using LayoutManager(?)

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

    private String getMonthString(int month) {
        if (month < 12 && month > -1) {
            return mMonths[month];
        } else {
            return "";
        }
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
}
