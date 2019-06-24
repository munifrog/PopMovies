package com.example.popularmovies.utils;

import android.net.Uri;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JsonManipulator {
    private static final String MOVIE_SORT_RESULTS     = "results";
    private static final String MOVIE_ID_TMDB          = "id";
    private static final String MOVIE_TITLE_CURRENT    = "title";
    private static final String MOVIE_TITLE_ORIGINAL   = "original_title";
    private static final String MOVIE_OVERVIEW         = "overview";
    private static final String MOVIE_IMAGE_PATH       = "poster_path";
    private static final String MOVIE_IMAGE_PATH_ALT   = "backdrop_path";
    private static final String MOVIE_IMAGE_START      = "/";
    private static final String MOVIE_RELEASE_DATE     = "release_date";
    private static final String MOVIE_RELEASE_SPLIT    = "-";
    private static final String MOVIE_VOTE_PER_TEN     = "vote_average";

    private static final String TRAILER_RESULTS        = "results";
    private static final String TRAILER_KEY            = "key";
    private static final String TRAILER_NAME           = "name";
    private static final String TRAILER_SITE           = "site";

    private static final String REVIEW_RESULTS         = "results";
    private static final String REVIEW_AUTHOR          = "author";
    private static final String REVIEW_CONTENT         = "content";
    private static final String REVIEW_URL             = "url";

    private static final String NOMINAL_NULL           = "null";

    static List<Movie> extractMoviesFromJson(String json) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray movieArray = jsonObject.getJSONArray(MOVIE_SORT_RESULTS);
            if (movieArray != null) {
                int length = movieArray.length();
                JSONObject object;
                for (int i = 0; i < length; i++) {
                    object = (JSONObject) movieArray.get(i);

                    long idTmdb = object.getLong(MOVIE_ID_TMDB);

                    // These are allowed to be empty strings ("")
                    String originalTitle = object.getString(MOVIE_TITLE_ORIGINAL);
                    String currentTitle = object.getString(MOVIE_TITLE_CURRENT);
                    String overview = object.getString(MOVIE_OVERVIEW);

                    // The image paths are not always present; process carefully
                    String imagePath = object.getString(MOVIE_IMAGE_PATH);
                    if (imagePath == null) {
                        imagePath = object.getString(MOVIE_IMAGE_PATH_ALT);
                    }
                    if (imagePath != null && !imagePath.equals(NOMINAL_NULL)) {
                        if (imagePath.startsWith(MOVIE_IMAGE_START)) {
                            // Leading slash is unnecessary and is easier to process when absent
                            imagePath = imagePath.substring(1);
                        }
                    }

                    // The release date is not always provided; process carefully
                    String releaseDateString = object.getString(MOVIE_RELEASE_DATE);
                    String[] sDate = releaseDateString.split(MOVIE_RELEASE_SPLIT);
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
                                    idTmdb,
                                    currentTitle,
                                    originalTitle,
                                    imagePath,
                                    overview,
                                    votePerOne,
                                    releaseDate,
                                    i
                            )
                    );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static List<Trailer> extractTrailersFromJson(String json) {
        List<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray trailerArray = jsonObject.getJSONArray(TRAILER_RESULTS);
            if (trailerArray != null) {
                int length = trailerArray.length();
                JSONObject object;
                for (int i = 0; i < length; i++) {
                    object = (JSONObject) trailerArray.get(i);

                    String key = object.getString(TRAILER_KEY);
                    String name = object.getString(TRAILER_NAME);
                    String site = object.getString(TRAILER_SITE);

                    Trailer newTrailer = new Trailer(
                            key,
                            name,
                            site
                    );

                    trailers.add(newTrailer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    public static List<Review> extractReviewsFromJson(String json) {
        List<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray reviewArray = jsonObject.getJSONArray(REVIEW_RESULTS);
            if (reviewArray != null) {
                int length = reviewArray.length();
                JSONObject object;
                for (int i = 0; i < length; i++) {
                    object = (JSONObject) reviewArray.get(i);

                    String author = object.getString(REVIEW_AUTHOR);
                    String content = object.getString(REVIEW_CONTENT);
                    String urlString = object.getString(REVIEW_URL);

                    URL url = null;
                    try {
                        url = new URL(urlString);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    Review newReview = new Review (
                            author,
                            content,
                            url
                    );

                    reviews.add(newReview);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
