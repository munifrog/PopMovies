package com.example.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.adapters.ReviewListAdapter;
import com.example.popularmovies.adapters.TrailerGridAdapter;
import com.example.popularmovies.model.CalendarConverter;
import com.example.popularmovies.model.LocalDatabase;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieViewModel;
import com.example.popularmovies.model.MovieViewModelFactory;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utils.HttpManipulator;
import com.example.popularmovies.utils.ReviewDiscoverer;
import com.example.popularmovies.utils.TrailerDiscoverer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements MovieConst,
        ReviewDiscoverer.ReviewDiscoveredListener,
        ReviewListAdapter.ReviewListClickListener,
        TrailerDiscoverer.TrailerDiscoveredListener,
        TrailerGridAdapter.TrailerGridClickListener
{
    private static Movie mMovie;
    private static Movie mFaveMovie;
    private static LocalDatabase mDatabase;
    private boolean mIsFave = false;
    private ReviewListAdapter mReviewsAdapter;
    private TrailerGridAdapter mTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView iv_poster = findViewById(R.id.iv_movie_poster);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_nameAlternate = findViewById(R.id.tv_name_alt);
        TextView tv_overview = findViewById(R.id.tv_plot);
        TextView tv_average = findViewById(R.id.tv_average);
        TextView tv_release = findViewById(R.id.tv_release);
        String [] months = getResources().getStringArray(R.array.months);

        Intent intent = getIntent();
        if(intent != null) {
            long id = intent.getLongExtra(MOVIE_ID, -1);
            String titleCurrent = intent.getStringExtra(MOVIE_TITLE_LOCAL);
            String titleOriginal = intent.getStringExtra(MOVIE_TITLE_ORIG);
            String imagePath = intent.getStringExtra(MOVIE_IMAGE_PATH);
            String overview = intent.getStringExtra(MOVIE_OVERVIEW);
            float rating = intent.getFloatExtra(MOVIE_RATING, 0.0f);
            Calendar releaseDate = CalendarConverter.convertToCalendar(
                    intent.getLongExtra(MOVIE_RELEASE, 0)
            );
            mMovie = new Movie(
                    id,
                    titleCurrent,
                    titleOriginal,
                    imagePath,
                    overview,
                    rating,
                    releaseDate,
                    -1
            );

            // Retrieve image first since it takes longest
            Uri imageUri = HttpManipulator.getImageUri(imagePath, ENUM_IMAGE_ORIG);
            if (imageUri != null && imageUri.getPath() != null && !imageUri.getPath().isEmpty()) {
                Picasso.get().load(imageUri).placeholder(R.drawable.poster_not_found).into(iv_poster);
            } else {
                iv_poster.setImageResource(R.drawable.poster_not_found);
            }
            // These also take extra time on a background thread to populate
            setupAdapters();
            extractTrailers();
            extractReviews();

            tv_name.setText(titleCurrent);

            if(titleCurrent.equals(titleOriginal)) {
                tv_nameAlternate.setHeight(0);
            } else {
                // Getting formatted strings is explained at these sites:
                // https://stackoverflow.com/questions/12627457/format-statement-in-a-string-resource-file
                // https://developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling
                titleOriginal = getString(
                        R.string.format_title_alternate,
                        titleOriginal
                );
                tv_nameAlternate.setText(titleOriginal);
            }

            tv_overview.setText(overview);

            int day = releaseDate.get(Calendar.DATE);
            int month = releaseDate.get(Calendar.MONTH);
            int year = releaseDate.get(Calendar.YEAR);
            String releaseString;
            if(year == 1970 && month == Calendar.JANUARY && day == 1) {
                releaseString = getString(R.string.unknown_release_date);
            } else {
                releaseString = getString(
                        R.string.format_release_date,
                        day,
                        months[month],
                        year
                );
            }
            tv_release.setText(releaseString);

            int averagePercent = Math.round(100.0f * rating);
            String average = getString(
                    R.string.format_average,
                    averagePercent
            );
            tv_average.setText(average);

            determineFavoriteStatus();
            showFavorite();
        }
    }

    private void showFavorite() {
        ImageView favoriteMarker = findViewById(R.id.iv_favorite);
        favoriteMarker.setImageResource(
                // See https://stackoverflow.com/questions/3201643/how-to-use-default-android-drawables
                (mIsFave ?
                        android.R.drawable.star_big_on :
                        android.R.drawable.star_big_off)
        );
    }

    public void toggleFavorite(View view) {
        mIsFave = !mIsFave;
        showFavorite();
    }

    private void determineFavoriteStatus() {
        MovieViewModelFactory factory = new MovieViewModelFactory(getApplication(), null);
        MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        mDatabase = viewModel.getDatabase(ENUM_STATE_FAVORITE);
        mDatabase.dao().loadById(mMovie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                mFaveMovie = movie;
                if (mFaveMovie != null) {
                    mIsFave = true;
                    showFavorite();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsFave) {
            if (mFaveMovie == null) {
                // Sort according to earliest date added
                mMovie.setIndex(
                        CalendarConverter.convertToLong(
                                Calendar.getInstance()
                        )
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() { mDatabase.dao().insertMovie(mMovie); }
                }).start();
            }
        } else {
            if (mFaveMovie != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() { mDatabase.dao().deleteMovie(mFaveMovie); }
                }).start();
            }
        }
    }

    public void extractTrailers() {
        Uri uri = HttpManipulator.getTrailerUri(mMovie.getId());
        new TrailerDiscoverer(this).execute(uri);
    }

    public void extractReviews() {
        Uri uri = HttpManipulator.getReviewsUri(mMovie.getId());
        new ReviewDiscoverer(this).execute(uri);
    }

    private void launchUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void setupAdapters() {
        RecyclerView reviewsRecyclerView = findViewById(R.id.rv_reviews_list);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewsAdapter = new ReviewListAdapter(this);
        reviewsRecyclerView.setAdapter(mReviewsAdapter);

        RecyclerView trailerRecyclerView = findViewById(R.id.rv_trailers_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        trailerRecyclerView.setLayoutManager(gridLayoutManager);
        mTrailersAdapter = new TrailerGridAdapter(this);
        trailerRecyclerView.setAdapter(mTrailersAdapter);
        trailerRecyclerView.stopScroll();
    }

    @Override
    public void onReviewExtractionComplete(List<Review> reviews) {
        mReviewsAdapter.setReviews(reviews);
    }

    @Override
    public void onReviewClick(URL toLaunch) {
        if (toLaunch != null) {
            launchUri(HttpManipulator.url2uri(toLaunch));
        }
    }

    @Override
    public void onTrailerExtractionComplete(List<Trailer> trailers) {
        mTrailersAdapter.setTrailers(trailers);
    }

    @Override
    public void onTrailerClick(String videoKey) {
        if(videoKey != null && !videoKey.isEmpty()) {
            launchUri(HttpManipulator.getSingleTrailerUri(videoKey));
        }
    }
}
