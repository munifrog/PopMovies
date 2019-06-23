package com.example.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "movie")
public class Movie {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "title_translated")
    private String mTitleCurrent;

    @ColumnInfo(name = "title_original")
    private String mTitleOriginal;

    @ColumnInfo(name = "image_path")
    private String mImagePath;

    @ColumnInfo(name = "description")
    private String mOverview;

    @ColumnInfo(name = "rating")
    private float mRating;

    @ColumnInfo(name = "release")
    private Calendar mRelease;

    @ColumnInfo(name = "sort_index")
    private long mIndex;

    public Movie(
            long id,
            String titleCurrent,
            String titleOriginal,
            String imagePath,
            String overview,
            float rating,
            Calendar release,
            long index)
    {
        mId = id;
        mTitleCurrent = titleCurrent;
        mTitleOriginal = titleOriginal;
        mImagePath = imagePath;
        mOverview = overview;
        mRating = rating;
        mRelease = release;
        mIndex = index;
    }

    public long getId() { return this.mId; }
    public void setId(long newId) { this.mId = newId; }

    public String getTitleCurrent() { return this.mTitleCurrent; }
    public void setTitleCurrent(String newTitle) { this.mTitleCurrent = newTitle; }

    public String getTitleOriginal() { return this.mTitleOriginal; }
    public void setTitleOriginal(String newTitle) { this.mTitleOriginal = newTitle; }

    public String getImagePath() { return this.mImagePath; }
    public void setImagePath(String newImageUrl) { this.mImagePath = newImageUrl; }

    public String getOverview() { return this.mOverview; }
    public void setOverview(String newOverview) { this.mOverview = newOverview; }

    public float getRating() { return this.mRating; }
    public void setRating(float newRating) { this.mRating = newRating; }

    public Calendar getRelease() { return this.mRelease; }
    public void setRelease(Calendar newRelease) { this.mRelease = newRelease; }

    public long getIndex() { return mIndex; }
    public void setIndex(long mIndex) { this.mIndex = mIndex; }
}
