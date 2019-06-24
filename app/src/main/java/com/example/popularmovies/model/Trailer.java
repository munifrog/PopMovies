package com.example.popularmovies.model;

public class Trailer {
    private String mTrailerKey;
    private String mTrailerName;
    private String mTrailerSite;

    public Trailer (
            String key,
            String name,
            String site
    ){
        mTrailerKey = key;
        mTrailerName = name;
        mTrailerSite = site;
    }

    public String getTrailerKey() { return mTrailerKey; }
    public void setTrailerKey(String mKey) { this.mTrailerKey = mKey; }

    public String getTrailerName() { return mTrailerName; }
    public void setTrailerName(String mName) { this.mTrailerName = mName; }

    public String getTrailerSite() { return mTrailerSite; }
    public void setTrailerSite(String mSite) { this.mTrailerSite = mSite; }
}
