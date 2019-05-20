package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.HttpManipulator;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class MovieDetailsActivity extends AppCompatActivity implements MovieConst {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView imageView = findViewById(R.id.iv_movie_poster);
        TextView name = findViewById(R.id.tv_name);
        TextView nameAlternate = findViewById(R.id.tv_name_alt);
        TextView overview = findViewById(R.id.tv_plot);
        TextView mAverage = findViewById(R.id.tv_average);
        TextView mRelease = findViewById(R.id.tv_release);
        String [] months = getResources().getStringArray(R.array.months);

        Intent intent = getIntent();
        if(intent != null) {
            Movie movie = intent.getParcelableExtra(ENTIRE_PARCELLED_MOVIE);

            // Retrieve image first since it takes longest
            Uri imageUri = HttpManipulator.getImageUri(movie.getImageUrl(), ENUM_IMAGE_ORIG);
            Picasso.get().load(imageUri).placeholder(R.drawable.poster_not_found).into(imageView);

            String titleCurrent = movie.getTitleCurrent();
            name.setText(titleCurrent);

            String titleOriginal = movie.getTitleOriginal();
            if(titleCurrent.equals(titleOriginal)) {
                nameAlternate.setHeight(0);
            } else {
                // Getting formatted strings is explained at these sites:
                // https://stackoverflow.com/questions/12627457/format-statement-in-a-string-resource-file
                // https://developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling
                titleOriginal = getString(
                        R.string.format_title_alternate,
                        titleOriginal
                );
                nameAlternate.setText(titleOriginal);
            }

            overview.setText(movie.getOverview());

            Calendar releaseDate = movie.getRelease();
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
            mRelease.setText(releaseString);

            int averagePercent = Math.round(100.0f * movie.getRating());
            String average = getString(
                    R.string.format_average,
                    averagePercent
            );
            mAverage.setText(average);
        }
    }
}
