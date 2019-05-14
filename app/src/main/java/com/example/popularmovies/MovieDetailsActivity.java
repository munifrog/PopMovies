package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.utils.HttpManipulator;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements MovieConst {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ImageView imageView = findViewById(R.id.iv_movie_poster);
        TextView name = findViewById(R.id.tv_name);
        TextView overview = findViewById(R.id.tv_plot);
        TextView mAverage = findViewById(R.id.tv_average);
        TextView mRelease = findViewById(R.id.tv_release);
        String [] months = getResources().getStringArray(R.array.months);

        Intent intent = getIntent();
        if(intent != null) {
            // Retrieve image first since it takes longest
            String imageName = intent.getStringExtra(DETAILS_IMAGE_URL);
            Uri imageUri = HttpManipulator.getImageUri(imageName);
            Log.v(TAG, "URL: \"" + imageUri + "\"");
            // TODO: Replace Avengers Poster with something generic (and not copyrighted)
            Picasso.get().load(imageUri).placeholder(R.drawable.avengers).into(imageView);

            String title = intent.getStringExtra(DETAILS_TITLE);
            name.setText(title);

            String plot = intent.getStringExtra(DETAILS_PLOT);
            overview.setText(plot);

            int day = intent.getIntExtra(DETAILS_RELEASE_DAY, 1);
            int month = intent.getIntExtra(DETAILS_RELEASE_MONTH, 1);
            int year = intent.getIntExtra(DETAILS_RELEASE_YEAR, 2019);
            // Getting formatted strings is explained at these sites:
            // https://stackoverflow.com/questions/12627457/format-statement-in-a-string-resource-file
            // https://developer.android.com/guide/topics/resources/string-resource.html#FormattingAndStyling
            String release = getString(
                    R.string.format_release_date,
                    day,
                    months[month],
                    year
            );
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
