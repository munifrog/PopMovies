package com.example.popularmovies;

public interface MovieConst {
    // This site suggested using an interface instead of a class:
    // https://stackoverflow.com/questions/3866190/java-constants-file
    int MAX_MOVIES_RETRIEVED = 20;

    int ENUM_STATE_POPULAR = 0;
    int ENUM_STATE_RATING = 1;
    int ENUM_STATE_FAVORITE = 2;

    int ENUM_IMAGE_ORIG = 0;
    int ENUM_IMAGE_0092 = 92;
    int ENUM_IMAGE_0154 = 154;
    int ENUM_IMAGE_0185 = 185;
    int ENUM_IMAGE_0342 = 342;
    int ENUM_IMAGE_0500 = 500;
    int ENUM_IMAGE_0780 = 780;

    int PREF_SPAN_LANDSCAPE = 4;
    int PREF_SPAN_PORTRAIT = 2;

    String TMDB_API_KEY           = "PLACEHOLDER";

    String MOVIE_ID               = "movie_id";
    String MOVIE_TITLE_LOCAL      = "movie_title_local";
    String MOVIE_TITLE_ORIG       = "movie_title_original";
    String MOVIE_IMAGE_PATH       = "movie_image_path";
    String MOVIE_OVERVIEW         = "movie_overview";
    String MOVIE_RATING           = "movie_rating";
    String MOVIE_RELEASE          = "movie_release";

    String SETTINGS_FILE          = "pop_movie_settings";
    String SETTINGS_SORT_LAST     = "last_sort";
}
