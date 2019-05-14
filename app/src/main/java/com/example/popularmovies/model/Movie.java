package com.example.popularmovies.model;

import java.util.Calendar;

public class Movie {
    private String mTitle;
    private String mImageUrl;
    private String mOverview;
    private float mRating;
    private Calendar mReleaseDate;

    public Movie(
            String title,
            String imageUrl,
            String overview,
            float rating,
            Calendar releaseDate)
    {
        mTitle = title;
        mImageUrl = imageUrl;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public String getTitle() { return this.mTitle; }
    public void setTitle(String newTitle) { this.mTitle = newTitle; }

    public String getImageUrl() { return this.mImageUrl; }
    public void setImageUrl(String newImageUrl) { this.mImageUrl = newImageUrl; }

    public String getOverview() { return this.mOverview; }
    public void setOverview(String newOverview) { this.mOverview = newOverview; }

    public float getRating() { return this.mRating; }
    public void setRating(float newRating) { this.mRating = newRating; }

    public Calendar getRelease() { return this.mReleaseDate; }
    public void setRelease(Calendar newReleaseDate) { this.mReleaseDate = newReleaseDate; }
}
