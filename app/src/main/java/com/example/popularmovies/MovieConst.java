package com.example.popularmovies;

public interface MovieConst {
    // This site suggested using an interface instead of a class:
    // https://stackoverflow.com/questions/3866190/java-constants-file
    int MAX_MOVIES_RETRIEVED = 20;

    int ENUM_SORT_POPULARITY_DESCENDING = 0;
    int ENUM_SORT_AVERAGE_RATING_DESCENDING = 1;
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

    String ENTIRE_PARCELLED_MOVIE = "movie_entire";

    String SETTINGS_FILE          = "pop_movie_settings";
    String SETTINGS_SORT_LAST     = "last_sort";
}
