package com.example.trendingtrails.Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trendingtrails.Models.Review;
import com.example.trendingtrails.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class TrailInfoViewAdapter extends RecyclerView.Adapter<TrailInfoViewAdapter.ViewHolder> {

    public TrailInfoViewAdapter.OnCardClickListener onCardClickListener;
    public List<Review> reviewList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RatingBar rating;
        ProgressBar intensity;
        TextView review;
        TextView reviewLabel;
        TrailInfoViewAdapter.OnCardClickListener onCardClickListener;

        public ViewHolder(View itemView, TrailInfoViewAdapter.OnCardClickListener onCardClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            rating = itemView.findViewById(R.id.infoRatingValue);
            intensity = itemView.findViewById(R.id.infoIntensityValue);
            review = itemView.findViewById(R.id.infoReviewValue);
            reviewLabel = itemView.findViewById(R.id.infoReviewLabel);
            this.onCardClickListener = onCardClickListener;
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition());


        }
    }

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    //Constructor
    public TrailInfoViewAdapter(List<Review> r, TrailInfoViewAdapter.OnCardClickListener onCardClickListener) {
        reviewList = r;
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    public TrailInfoViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reviewView = inflater.inflate(R.layout.reviews, parent, false);
        TrailInfoViewAdapter.ViewHolder viewHolder = new TrailInfoViewAdapter.ViewHolder(reviewView, onCardClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailInfoViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.rating.setRating(reviewList.get(position).rating);
        viewHolder.rating.setIsIndicator(true);
        viewHolder.intensity.setProgress(reviewList.get(position).intensity * 10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
        if(reviewList.get(position).review.trim().isEmpty()) {
            viewHolder.review.setText("-"+reviewList.get(position).name + " (" + dateFormat.format(reviewList.get(position).date) + ")");
            viewHolder.reviewLabel.setVisibility(View.INVISIBLE);
        }
        else
            viewHolder.review.setText(reviewList.get(position).review + "\n-"+reviewList.get(position).name
                    + " (" + dateFormat.format(reviewList.get(position).date) + ")");
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
