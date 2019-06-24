package com.example.popularmovies.model;

import java.net.URL;

public class Review {
    private String mReviewAuthor;
    private String mReviewContent;
    private URL mReviewUrl;

    public Review (
            String author,
            String content,
            URL url
    ){
        mReviewAuthor = author;
        mReviewContent = content;
        mReviewUrl = url;
    }

    public String getReviewAuthor() { return mReviewAuthor; }
    public void setReviewAuthor(String mReviewAuthor) { this.mReviewAuthor = mReviewAuthor; }

    public String getReviewContent() { return mReviewContent; }
    public void setReviewContent(String mReviewContent) { this.mReviewContent = mReviewContent; }

    public URL getReviewUrl() { return mReviewUrl; }
    public void setReviewUrl(URL mReviewUrl) { this.mReviewUrl = mReviewUrl; }
}
