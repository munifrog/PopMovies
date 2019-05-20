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
    private static final String MOVIE_TITLE_CURRENT  = "title";
    private static final String MOVIE_TITLE_ORIGINAL = "original_title";
    private static final String MOVIE_OVERVIEW       = "overview";
    private static final String MOVIE_IMAGE_PATH     = "poster_path";
    private static final String MOVIE_IMAGE_PATH_ALT = "backdrop_path";
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

                    // These are allowed to be empty strings ("")
                    String originalTitle = object.getString(MOVIE_TITLE_ORIGINAL);
                    String currentTitle = object.getString(MOVIE_TITLE_CURRENT);
                    String overview = object.getString(MOVIE_OVERVIEW);

                    // The image paths are not always present; process carefully
                    String imagePath = object.getString(MOVIE_IMAGE_PATH);
                    if(imagePath == null) {
                        imagePath = object.getString(MOVIE_IMAGE_PATH_ALT);
                    }
                    if(imagePath != null && !imagePath.equals("null")) {
                        if(imagePath.startsWith("/")) {
                            // Leading slash is unnecessary and is easier to process when absent
                            imagePath = imagePath.substring(1);
                        }
                    }

                    // The release date is not always provided; process carefully
                    String releaseDateString = object.getString(MOVIE_RELEASE_DATE);
                    String [] sDate = releaseDateString.split("-");
                    Calendar releaseDate = Calendar.getInstance();
                    if (sDate.length == 3) {
                        releaseDate.set(Calendar.YEAR, Integer.valueOf(sDate[0]));
                        releaseDate.set(Calendar.MONTH, Integer.valueOf(sDate[1]));
                        releaseDate.set(Calendar.DATE, Integer.valueOf(sDate[2]));
                    } else {
                        // Set to beginning of Epoch; unlikely any movie was released at this time
                        releaseDate.set(Calendar.YEAR, 1970);
                        releaseDate.set(Calendar.MONTH, Calendar.JANUARY);
                        releaseDate.set(Calendar.DATE, 1);
                        releaseDate.set(Calendar.HOUR_OF_DAY, 0);
                        releaseDate.set(Calendar.MINUTE, 0);
                        releaseDate.set(Calendar.SECOND, 0);
                        releaseDate.set(Calendar.MILLISECOND, 0);
                    }

                    // Unlikely any movie gets into the popular or highly rated lists without this value
                    String votePerTen = object.getString(MOVIE_VOTE_PER_TEN);
                    float votePerOne = Float.valueOf(votePerTen) / 10.0f;

                    movies.add(
                            new Movie(
                                    currentTitle,
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
