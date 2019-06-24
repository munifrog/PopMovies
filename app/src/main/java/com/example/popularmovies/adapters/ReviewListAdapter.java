package com.example.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Review;

import java.net.URL;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {
    private final ReviewListClickListener mListener;
    private static List<Review> mReviews;

    public ReviewListAdapter(ReviewListClickListener listener) {
        mListener = listener;
    }

    public interface ReviewListClickListener {
        void onReviewClick(URL url);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mReviews == null) {
            return 0;
        } else {
            return mReviews.size();
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mAuthor;
        TextView mContent;

        ReviewViewHolder(View item) {
            super(item);
            mAuthor = item.findViewById(R.id.tv_author);
            mContent = item.findViewById(R.id.tv_content);
            item.setOnClickListener(this);
        }

        void bind(int position){
            Review review = mReviews.get(position);
            mAuthor.setText(review.getReviewAuthor());
            mContent.setText(review.getReviewContent());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (mReviews != null) {
                Review clicked = mReviews.get(position);
                mListener.onReviewClick(clicked.getReviewUrl());
            }
        }
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
