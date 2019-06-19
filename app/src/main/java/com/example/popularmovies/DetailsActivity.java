package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.HttpManipulator;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity implements MovieConst {
    private static Movie mMovie;

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
            mMovie = intent.getParcelableExtra(ENTIRE_PARCELLED_MOVIE);

            // Retrieve image first since it takes longest
            Uri imageUri = HttpManipulator.getImageUri(mMovie.getImageUrl(), ENUM_IMAGE_ORIG);
            if (imageUri != null && imageUri.getPath() != null && !imageUri.getPath().isEmpty()) {
                Picasso.get().load(imageUri).placeholder(R.drawable.poster_not_found).into(iv_poster);
            } else {
                iv_poster.setImageResource(R.drawable.poster_not_found);
            }

            String titleCurrent = mMovie.getTitleCurrent();
            tv_name.setText(titleCurrent);

            String titleOriginal = mMovie.getTitleOriginal();
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

            tv_overview.setText(mMovie.getOverview());

            Calendar releaseDate = mMovie.getRelease();
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

            int averagePercent = Math.round(100.0f * mMovie.getRating());
            String average = getString(
                    R.string.format_average,
                    averagePercent
            );
            tv_average.setText(average);

            showFavorite();
        }
    }

    private void showFavorite() {
        if (mMovie != null) {
            ImageView favoriteMarker = findViewById(R.id.iv_favorite);
            favoriteMarker.setImageResource(
                    // See https://stackoverflow.com/questions/3201643/how-to-use-default-android-drawables
                    (mMovie.getFavorite() ?
                            android.R.drawable.star_big_on :
                            android.R.drawable.star_big_off)
            );
        }
    }

    public void toggleFavorite(View view) {
        mMovie.setFavorite(!mMovie.getFavorite());
        showFavorite();
    }
}
