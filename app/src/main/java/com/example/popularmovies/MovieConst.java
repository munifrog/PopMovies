package com.example.popularmovies;

public interface MovieConst {
    // This site suggested using an interface instead of a class:
    // https://stackoverflow.com/questions/3866190/java-constants-file
    int MAX_MOVIES_RETRIEVED = 20;

    int ENUM_SORT_POPULARITY_DESCENDING = 0;
    int ENUM_SORT_AVERAGE_RATING_DESCENDING = 1;

    String TMDB_API_KEY           = "PLACEHOLDER";

    String DETAILS_TITLE          = "movie_title";
    String DETAILS_PLOT           = "movie_plot";
    String DETAILS_RELEASE_DAY    = "movie_release_dd";
    String DETAILS_RELEASE_MONTH  = "movie_release_mm";
    String DETAILS_RELEASE_YEAR   = "movie_release_yyyy";
    String DETAILS_AVERAGE_RATING = "movie_rating";
    String DETAILS_IMAGE_URL      = "movie_image_url";

    String SETTINGS_FILE          = "pop_movie_settings";
    String SETTINGS_SORT_LAST     = "last_sort";

}
