package com.example.popularmovies.utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.example.popularmovies.MovieConst;

import java.io.IOException;
import java.io.InputStream;
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
    private final static String TMDB_PATH_IMAGE_T = "t";
    private final static String TMDB_PATH_IMAGE_P = "p";
    private final static String TMDB_PATH_IMAGE_ORIG = "original";
    private final static String TMDB_PATH_IMAGE_WIDE = "w";

    private final static String TMDB_API_Q_KEY = "api_key";
    private final static String TMDB_API_V_KEY = MovieConst.TMDB_API_KEY;

    private final static String TMDB_API_Q_SORT = "sort_by";
    private final static String TMDB_API_V_SORT_POPULARITY = "popularity.desc";
    private final static String TMDB_API_V_SORT_RATING = "vote_average.desc";

    private final static String TMDB_API_Q_LANG = "language";
    private final static String TMDB_API_V_LANG = "en";
    private final static String TMDB_API_Q_LIST_STYLE = "list_style";
    private final static String TMDB_API_V_LIST_STYLE = "1";
    private final static String TMDB_API_Q_MEDIA_TYPE = "media_type";
    private final static String TMDB_API_V_MEDIA_TYPE = "movie";
    private final static String TMDB_API_Q_RELEASE = "primary_release_year";
    private final static String TMDB_API_V_RELEASE = "2019";
    private final static String TMDB_API_Q_VOTE_COUNT = "vote_count.gte";
    private final static String TMDB_API_V_VOTE_COUNT = "0";

    private final static String TMDB_API_Q_PAGE = "page"; // Values are numbers like 1, 2, etc...

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
                .appendQueryParameter(TMDB_API_Q_LANG, TMDB_API_V_LANG)
                .appendQueryParameter(TMDB_API_Q_LIST_STYLE, TMDB_API_V_LIST_STYLE)
                .appendQueryParameter(TMDB_API_Q_MEDIA_TYPE, TMDB_API_V_MEDIA_TYPE)
                .appendQueryParameter(TMDB_API_Q_RELEASE, TMDB_API_V_RELEASE)
                .appendQueryParameter(TMDB_API_Q_VOTE_COUNT, TMDB_API_V_VOTE_COUNT)
                ;
    }

    private static Uri.Builder baseImageUriBuilder() {
        return new Uri.Builder()
                .scheme(TMDB_SCHEME)
                .authority(TMDB_IMAGE_AUTHORITY)
                ;
    }

    private static Uri.Builder getSortedBase() {
        return baseMovieUriBuilder()
                .appendPath(TMDB_PATH_DISCOVER)
                .appendPath(TMDB_PATH_MOVIE);
    }

    public static Uri getSortedUri(int sortBy, int page) {
        Uri uri = null;
        switch(sortBy) {
            case MovieConst.ENUM_STATE_POPULAR:
                uri = getSortedBase()
                        .appendQueryParameter(TMDB_API_Q_SORT, TMDB_API_V_SORT_POPULARITY)
                        .appendQueryParameter(TMDB_API_Q_PAGE, String.valueOf(page))
                        .build();
                break;
            case MovieConst.ENUM_STATE_RATING:
                uri = getSortedBase()
                        .appendQueryParameter(TMDB_API_Q_SORT, TMDB_API_V_SORT_RATING)
                        .appendQueryParameter(TMDB_API_Q_PAGE, String.valueOf(page))
                        .build();
                break;
        }
        return uri;
    }

    public static Uri getImageUri(String imagePathAndName, int size) {
        String imageSize = (size == 0 ? TMDB_PATH_IMAGE_ORIG : TMDB_PATH_IMAGE_WIDE + size);

        Uri uri;
        if (imagePathAndName != null) {
            uri = baseImageUriBuilder()
                    .appendPath(TMDB_PATH_IMAGE_T)
                    .appendPath(TMDB_PATH_IMAGE_P)
                    .appendPath(imageSize)
                    .appendPath(imagePathAndName)
                    .build()
            ;
        } else {
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

    // https://stackoverflow.com/questions/26689464/how-to-download-image-file-by-using-okhttpclient-in-java
    static Drawable getImage(URL url) {
        Drawable image = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody rBody = response.body();
            if(rBody != null) {
                InputStream stream = rBody.byteStream();
                image = Drawable.createFromStream(stream, "useless");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
