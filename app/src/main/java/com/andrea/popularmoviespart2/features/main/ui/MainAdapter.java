package com.andrea.popularmoviespart2.features.main.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.util.GlideUtil;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private ListItemClickListener onClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    MainAdapter(@NonNull ListItemClickListener onClickListener, @NonNull List<Movie> movieList) {
        this.onClickListener = onClickListener;
        this.movieList = movieList;
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override public int getItemCount() {
        return movieList != null && movieList.size() > 0 ? movieList.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView moviePosterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.imageview_movie_poster_list_item);
            itemView.setOnClickListener(this);
        }

        void bind(int listItem) {
            GlideUtil.displayImage(movieList.get(listItem).getPosterPath(), moviePosterImageView);
        }

        @Override public void onClick(View view) {
            onClickListener.onListItemClick(movieList.get(getAdapterPosition()));
        }
    }
}
