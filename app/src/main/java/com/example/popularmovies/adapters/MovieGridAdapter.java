package com.example.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.example.popularmovies.utils.HttpManipulator;
import com.squareup.picasso.Picasso;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {
    private final GridClickListener mListener;

    private int mImageSize;
    private String[] mImages;

    public MovieGridAdapter(GridClickListener listener, int imageSize) {
        // The adapter only needs the images to do its job
        mListener = listener;
        mImageSize = imageSize;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mImages == null) {
            return 0;
        } else {
            return mImages.length;
        }
    }

    public interface GridClickListener {
        void onClick(int position);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMovieView;

        MovieViewHolder(View item) {
            super(item);
            mMovieView = item.findViewById(R.id.iv_movie);
            item.setOnClickListener(this);
        }

        void bind(int position){
            Uri imageUri = HttpManipulator.getImageUri(mImages[position], mImageSize);
            // COMPLETED: Your app might crash when passing null/empty/malformed Urls. Check your path
            //  url before loading the image. [https://github.com/square/picasso/issues/609]
            //  You can also use the additional option error and placeholder in Picasso here to
            //  avoid crashing (error also takes a drawable image)
            if (imageUri != null && imageUri.getPath() != null && !imageUri.getPath().isEmpty()) {
                Picasso.get().load(imageUri).placeholder(R.drawable.poster_not_found).into(mMovieView);
            } else {
                mMovieView.setImageResource(R.drawable.poster_not_found);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onClick(position);
        }
    }

    public void setMovieImages(String [] images) {
        mImages = images;
        notifyDataSetChanged();
    }
}
