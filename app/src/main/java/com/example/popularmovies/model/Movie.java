package com.example.popularmovies.model;

import android.graphics.drawable.Drawable;

import java.util.Calendar;

public class Movie {
    private String mTitleCurrent;
    private String mTitleOriginal;
    private String mImagePath;
    private Drawable mImageSmall;
    private String mOverview;
    private float mRating;
    private Calendar mReleaseDate;

    public Movie(
            String titleCurrent,
            String titleOriginal,
            String imagePath,
            Drawable imageSmall,
            String overview,
            float rating,
            Calendar releaseDate)
    {
        mTitleCurrent = titleCurrent;
        mTitleOriginal = titleOriginal;
        mImagePath = imagePath;
        mImageSmall = imageSmall;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public String getTitleCurrent() { return this.mTitleCurrent; }
    public void setTitleCurrent(String newTitle) { this.mTitleCurrent = newTitle; }

    public String getTitleOriginal() { return this.mTitleOriginal; }
    public void setTitleOriginal(String newTitle) { this.mTitleOriginal = newTitle; }

    public String getImageUrl() { return this.mImagePath; }
    public void setImageUrl(String newImageUrl) { this.mImagePath = newImageUrl; }

    public Drawable getImageSmall() { return this.mImageSmall; }
    public void setImageSmall(Drawable newImageSmall) { this.mImageSmall = newImageSmall; }

    public String getOverview() { return this.mOverview; }
    public void setOverview(String newOverview) { this.mOverview = newOverview; }

    public float getRating() { return this.mRating; }
    public void setRating(float newRating) { this.mRating = newRating; }

    public Calendar getRelease() { return this.mReleaseDate; }
    public void setRelease(Calendar newReleaseDate) { this.mReleaseDate = newReleaseDate; }
}
