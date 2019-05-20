package com.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.utils.HttpManipulator;
import com.squareup.picasso.Picasso;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MovieViewHolder> {
    private final GridClickListener mListener;

    private int mItemCount;
    private int mImageSize;

    private String[] mImages;


    GridAdapter(String [] images, GridClickListener listener, int imageSize) {
        mItemCount = images.length;
        mListener = listener;
        mImages = images;
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
        return mItemCount;
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
            Picasso.get().load(imageUri).placeholder(R.drawable.poster_not_found).into(mMovieView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onClick(position);
        }
    }
}
