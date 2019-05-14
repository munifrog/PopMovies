package com.example.popularmovies.utils;

import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JsonManipulator {
    private static final String MOVIE_SORT_RESULTS   = "results";
    private static final String MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String MOVIE_OVERVIEW       = "overview";
    private static final String MOVIE_IMAGE_PATH     = "poster_path";
    private static final String MOVIE_RELEASE_DATE   = "release_date";
    private static final String MOVIE_VOTE_PER_TEN   = "vote_average";

    public static List<Movie> extractMoviesFromJson(String json) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray movieArray = jsonObject.getJSONArray(MOVIE_SORT_RESULTS);
            if(movieArray != null) {
                int length = movieArray.length();
                JSONObject object;
                for(int i = 0; i < length; i++) {
                    object = (JSONObject) movieArray.get(i);
                    String originalTitle = object.getString(MOVIE_ORIGINAL_TITLE);
                    String overview = object.getString(MOVIE_OVERVIEW);
                    String imagePath = object.getString(MOVIE_IMAGE_PATH);
                    if(imagePath.startsWith("/")) {
                        // Leading slash is unnecessary and is easier to process when absent
                        imagePath = imagePath.substring(1);
                    }
                    String releaseDateString = object.getString(MOVIE_RELEASE_DATE);
                    String votePerTen = object.getString(MOVIE_VOTE_PER_TEN);

                    Calendar releaseDate = null;
                    String [] sDate = releaseDateString.split("-");
                    if (sDate.length == 3) {
                        releaseDate = Calendar.getInstance();
                        releaseDate.set(Calendar.YEAR, Integer.valueOf(sDate[0]));
                        releaseDate.set(Calendar.MONTH, Integer.valueOf(sDate[1]));
                        releaseDate.set(Calendar.DATE, Integer.valueOf(sDate[2]));
                    }

                    float votePerOne = Float.valueOf(votePerTen) / 10.0f;

                    movies.add(
                            new Movie(
                                    originalTitle,
                                    imagePath,
                                    overview,
                                    votePerOne,
                                    releaseDate
                            )
                    );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
