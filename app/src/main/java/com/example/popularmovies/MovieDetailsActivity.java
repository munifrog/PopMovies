package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
            // Retrieve image first since it takes longest
            String imageName = intent.getStringExtra(DETAILS_IMAGE_URL);
            Uri imageUri = HttpManipulator.getImageUri(imageName, ENUM_IMAGE_ORIG);
            Picasso.get().load(imageUri).placeholder(R.drawable.poster_not_found).into(imageView);

            String titleCurrent = intent.getStringExtra(DETAILS_TITLE_CURRENT);
            name.setText(titleCurrent);

            String titleOriginal = intent.getStringExtra(DETAILS_TITLE_ORIG);
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

            String plot = intent.getStringExtra(DETAILS_PLOT);
            overview.setText(plot);

            int day = intent.getIntExtra(DETAILS_RELEASE_DAY, 1);
            int month = intent.getIntExtra(DETAILS_RELEASE_MONTH, 1);
            int year = intent.getIntExtra(DETAILS_RELEASE_YEAR, 2019);

            String release;
            if(year == 1970 && month == Calendar.JANUARY && day == 1) {
                release = getString(R.string.unknown_release_date);
            } else {
                release = getString(
                        R.string.format_release_date,
                        day,
                        months[month],
                        year
                );
            }

            mRelease.setText(release);

            int averagePercent = intent.getIntExtra(DETAILS_AVERAGE_RATING, 0);
            String average = getString(
                    R.string.format_average,
                    averagePercent
            );
            mAverage.setText(average);
        }
    }
}
