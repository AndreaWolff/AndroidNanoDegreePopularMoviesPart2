package com.andrea.popularmoviespart2.features.main.ui;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.data.MovieContract;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.util.GlideUtil;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMoviesViewHolder> {

    private Cursor cursor;
    private FavoriteMoviesAdapter.ListItemClickListener onClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
    }

    public FavoriteMoviesAdapter(@NonNull FavoriteMoviesAdapter.ListItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public FavoriteMoviesAdapter.FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_list_item, parent, false);
        return new FavoriteMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMoviesAdapter.FavoriteMoviesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;
        return cursor.getCount();
    }

    void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView moviePosterImageView;

        FavoriteMoviesViewHolder(View itemView) {
            super(itemView);

            this.moviePosterImageView = itemView.findViewById(R.id.imageview_movie_poster_list_item);
            itemView.setOnClickListener(this);
        }

        void bind(int listItem) {
            cursor.moveToPosition(listItem);
            int columnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
            String string = cursor.getString(columnIndex);
            GlideUtil.displayImage(string, moviePosterImageView);
        }

        @Override
        public void onClick(View view) {
            cursor.moveToPosition(getAdapterPosition());
            onClickListener.onListItemClick(configureMovie());
        }

        @NonNull
        private Movie configureMovie() {
            int columnMovieId = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            String movieId = cursor.getString(columnMovieId);
            int columnMovieTitle = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
            String movieTitle = cursor.getString(columnMovieTitle);
            int columnMovieReleaseDate = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
            String movieReleaseDate = cursor.getString(columnMovieReleaseDate);
            int columnMovieVoteAverage = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE);
            float movieVoteAverage = cursor.getFloat(columnMovieVoteAverage);
            int columnMoviePlotSynopsis = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS);
            String moviePlotSynopsis = cursor.getString(columnMoviePlotSynopsis);
            int columnMoviePoster = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
            String moviePoster = cursor.getString(columnMoviePoster);
            int columnMovieBackdropPhoto = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PHOTO_PATH);
            String movieBackdropPhoto = cursor.getString(columnMovieBackdropPhoto);
            int columnMovieFavorite = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE);
            int movieFavorite = cursor.getInt(columnMovieFavorite);

            boolean isMovieFavorited;
            isMovieFavorited = movieFavorite == 1;

            return new Movie(movieId, movieTitle, movieReleaseDate, movieVoteAverage, moviePlotSynopsis, moviePoster, movieBackdropPhoto, isMovieFavorited);
        }
    }
}