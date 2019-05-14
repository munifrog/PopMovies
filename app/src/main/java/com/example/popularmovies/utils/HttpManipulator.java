package com.example.popularmovies.utils;

import android.net.Uri;

import com.example.popularmovies.MovieConst;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpManipulator {

    private final static String TMDB_SCHEME = "https";
    private final static String TMDB_API_AUTHORITY = "api.themoviedb.org";
    private final static String TMDB_IMAGE_AUTHORITY = "image.tmdb.org";
    private final static String TMDB_PATH_API_LEVEL = "3";
    private final static String TMDB_PATH_DISCOVER = "discover";
    private final static String TMDB_PATH_MOVIE = "movie";
    private final static String TMDB_PATH_IMAGE_SM = "t/p/w185"; // Recommended by Udacity
    private final static String TMDB_PATH_IMAGE_MD = "t/p/w500"; // Examples on TMDb API site
    private final static String TMDB_PATH_IMAGE_LG = "t/p/original";
    private final static String TMDB_PATH_IMAGE = TMDB_PATH_IMAGE_LG;

    private final static String TMDB_API_Q_KEY = "api_key";
    private final static String TMDB_API_V_KEY = MovieConst.TMDB_API_KEY;

    private final static String TMDB_API_Q_SORT = "sort_by";
    private final static String TMDB_API_V_SORT_POPULARITY = "popularity.desc";
    private final static String TMDB_API_V_SORT_RATING = "vote_average.desc";

    public static URL uri2url(Uri uri) {
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static Uri.Builder baseMovieUriBuilder() {
        return new Uri.Builder()
                .scheme(TMDB_SCHEME)
                .authority(TMDB_API_AUTHORITY)
                .path(TMDB_PATH_API_LEVEL)
                .appendQueryParameter(TMDB_API_Q_KEY, TMDB_API_V_KEY)
                ;
    }

    private static Uri.Builder baseImageUriBuilder() {
        return new Uri.Builder()
                .scheme(TMDB_SCHEME)
                .authority(TMDB_IMAGE_AUTHORITY)
                .path(TMDB_PATH_IMAGE)
                ;
    }

    private static Uri.Builder getSortedBase() {
        return baseMovieUriBuilder()
                .appendPath(TMDB_PATH_DISCOVER)
                .appendPath(TMDB_PATH_MOVIE);
    }

    public static Uri getSortedUri(int sortBy) {
        Uri uri = null;
        switch(sortBy) {
            case MovieConst.ENUM_SORT_POPULARITY_DESCENDING:
                uri = getSortedBase()
                        .appendQueryParameter(TMDB_API_Q_SORT, TMDB_API_V_SORT_POPULARITY)
                        .build();
                break;
            case MovieConst.ENUM_SORT_AVERAGE_RATING_DESCENDING:
                uri = getSortedBase()
                        .appendQueryParameter(TMDB_API_Q_SORT, TMDB_API_V_SORT_RATING)
                        .build();
                break;
        }
        return uri;
    }

    public static Uri getImageUri(String imagePathAndName) {
        Uri uri;
        if (imagePathAndName != null) {
            uri = baseImageUriBuilder().appendPath(imagePathAndName).build();
        } else {
            // TODO: Is there a default image URL at TMDb? or else I could use my own with Picasso default image ...
            uri = baseImageUriBuilder().appendPath(imagePathAndName).build();
        }
        return uri;
    }

    // I obtained the explanation for retrieving URLs from http://square.github.io/okhttp/
    // Mentioned as alternative in Udacity Lesson 2.9: Connect to the Internet; Fetching an HTTP Request
    public static String getResponse(URL url) {
        String body = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody rBody = response.body();
            if(rBody != null) {
                body = rBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
