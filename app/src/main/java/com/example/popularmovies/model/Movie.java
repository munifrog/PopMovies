package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Movie implements Parcelable {
    private String mTitleCurrent;
    private String mTitleOriginal;
    private String mImagePath;
    private String mOverview;
    private float mRating;
    private Calendar mReleaseDate;

    private Movie (Parcel parcel) {
        // Note these must appear in same order as writeToParcel that creates the Parcel
        mTitleCurrent = parcel.readString();
        mTitleOriginal = parcel.readString();
        mImagePath = parcel.readString();
        mOverview = parcel.readString();
        mRating = parcel.readFloat();
        mReleaseDate = Calendar.getInstance();
        int year = parcel.readInt();
        mReleaseDate.set(Calendar.YEAR, year);
        int month = parcel.readInt();
        mReleaseDate.set(Calendar.MONTH, month);
        int date = parcel.readInt();
        mReleaseDate.set(Calendar.DATE, date);
    }

    public Movie(
            String titleCurrent,
            String titleOriginal,
            String imagePath,
            String overview,
            float rating,
            Calendar releaseDate)
    {
        mTitleCurrent = titleCurrent;
        mTitleOriginal = titleOriginal;
        mImagePath = imagePath;
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

    public String getOverview() { return this.mOverview; }
    public void setOverview(String newOverview) { this.mOverview = newOverview; }

    public float getRating() { return this.mRating; }
    public void setRating(float newRating) { this.mRating = newRating; }

    public Calendar getRelease() { return this.mReleaseDate; }
    public void setRelease(Calendar newReleaseDate) { this.mReleaseDate = newReleaseDate; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // Note these must appear in same order as constructor that takes Parcel
        parcel.writeString(mTitleCurrent);
        parcel.writeString(mTitleOriginal);
        parcel.writeString(mImagePath);
        parcel.writeString(mOverview);
        parcel.writeFloat(mRating);
        parcel.writeInt(mReleaseDate.get(Calendar.YEAR));
        parcel.writeInt(mReleaseDate.get(Calendar.MONTH));
        parcel.writeInt(mReleaseDate.get(Calendar.DATE));
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
