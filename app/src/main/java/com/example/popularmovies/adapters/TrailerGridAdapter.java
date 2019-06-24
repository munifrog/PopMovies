package com.example.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Trailer;

import java.util.List;

public class TrailerGridAdapter extends RecyclerView.Adapter<TrailerGridAdapter.TrailerViewHolder> {
    private final TrailerGridClickListener mListener;
    private static List<Trailer> mTrailers;

    public TrailerGridAdapter(TrailerGridClickListener listener) {
        mListener = listener;
    }

    public interface TrailerGridClickListener {
        void onTrailerClick(String videoTag);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mTrailers == null) {
            return 0;
        } else {
            return mTrailers.size();
        }
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mMovieView;
        TextView mCount;

        TrailerViewHolder(View item) {
            super(item);
            mMovieView = item.findViewById(R.id.iv_icon);
            mCount = item.findViewById(R.id.tv_trailer_count);
            item.setOnClickListener(this);
        }

        void bind(int position){
            mMovieView.setImageResource(R.drawable.movie_trailer);
            mMovieView.setContentDescription(mTrailers.get(position).getTrailerName());
            mCount.setText(Integer.toString(position + 1));
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (mTrailers != null) {
                Trailer trailer = mTrailers.get(position);
                mListener.onTrailerClick(trailer.getTrailerKey());
            }
        }
    }

    public void setTrailers(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }
}
