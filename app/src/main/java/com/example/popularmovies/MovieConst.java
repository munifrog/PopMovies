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

    String TMDB_API_KEY           = "PLACEHOLDER";

    String DETAILS_TITLE_ORIG     = "movie_title_original";
    String DETAILS_TITLE_CURRENT  = "movie_title_current";
    String DETAILS_PLOT           = "movie_plot";
    String DETAILS_RELEASE_DAY    = "movie_release_dd";
    String DETAILS_RELEASE_MONTH  = "movie_release_mm";
    String DETAILS_RELEASE_YEAR   = "movie_release_yyyy";
    String DETAILS_AVERAGE_RATING = "movie_rating";
    String DETAILS_IMAGE_URL      = "movie_image_url";

    String SETTINGS_FILE          = "pop_movie_settings";
    String SETTINGS_SORT_LAST     = "last_sort";
}
