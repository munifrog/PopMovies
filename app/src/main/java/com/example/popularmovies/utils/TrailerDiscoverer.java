package com.example.popularmovies.utils;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.popularmovies.model.Trailer;

import java.util.List;

public class TrailerDiscoverer extends AsyncTask<Uri, Void, List<Trailer>> {
    private TrailerDiscoveredListener mListener;

    public TrailerDiscoverer(TrailerDiscoveredListener listener) {
        mListener = listener;
    }

    public interface TrailerDiscoveredListener {
        void onTrailerExtractionComplete(List<Trailer> trailers);
    }

    @Override
    protected List<Trailer> doInBackground(Uri... uris) {
        List<Trailer> trailers = null;
        try {
            Uri uri = uris[0];
            String json = HttpManipulator.getResponse(HttpManipulator.uri2url(uri));
            trailers = JsonManipulator.extractTrailersFromJson(json);
        } catch (RuntimeException e) {
            // Internet unavailable
        }
        return trailers;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        super.onPostExecute(trailers);
        mListener.onTrailerExtractionComplete(trailers);
    }
}