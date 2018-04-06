package com.andrea.popularmoviespart2.features.details.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.domain.MovieReview;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MovieReviewsViewHolder> {

    private List<MovieReview> movieReviews;

    DetailsAdapter(@NonNull List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @Override
    public MovieReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_list_item, parent, false);
        return new MovieReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieReviews != null && movieReviews.size() > 0 ? movieReviews.size() : 0;
    }

    class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        private TextView authorTextView;
        private TextView reviewContentTextView;

        MovieReviewsViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.detailsUserReviewAuthor);
            reviewContentTextView = itemView.findViewById(R.id.detailsUserReview);
        }

        void bind(int listItem) {
            authorTextView.setText(movieReviews.get(listItem).getAuthor());
            String content = "\"" + movieReviews.get(listItem).getContent() + "\"";
            reviewContentTextView.setText(content);
        }
    }
}
