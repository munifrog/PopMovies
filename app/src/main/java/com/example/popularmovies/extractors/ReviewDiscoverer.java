package com.example.popularmovies.extractors;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.popularmovies.model.Review;
import com.example.popularmovies.utils.HttpManipulator;
import com.example.popularmovies.utils.JsonManipulator;

import java.util.List;

public class ReviewDiscoverer extends AsyncTask<Uri, Void, List<Review>> {
    private ReviewDiscoveredListener mListener;

    public ReviewDiscoverer(ReviewDiscoveredListener listener) {
        mListener = listener;
    }

    public interface ReviewDiscoveredListener {
        void onReviewExtractionComplete(List<Review> reviews);
        void onInternetFailure();
    }

    @Override
    protected List<Review> doInBackground(Uri... uris) {
        List<Review> reviews = null;
        try {
            Uri uri = uris[0];
            String json = HttpManipulator.getResponse(HttpManipulator.uri2url(uri));
            reviews = JsonManipulator.extractReviewsFromJson(json);
        } catch (RuntimeException e) {
            mListener.onInternetFailure();
        }
        return reviews;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        super.onPostExecute(reviews);
        mListener.onReviewExtractionComplete(reviews);
    }
}