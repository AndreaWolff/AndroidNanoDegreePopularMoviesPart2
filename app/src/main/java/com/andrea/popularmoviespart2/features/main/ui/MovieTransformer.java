package com.andrea.popularmoviespart2.features.main.ui;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.data.MovieContract;
import com.andrea.popularmoviespart2.features.common.domain.Movie;

class MovieTransformer {

    /**
     * Transformers the selected cursor item into a Movie.
     */
    @NonNull static Movie transformToMovie(@NonNull Cursor cursor) {
        String movieId = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        String movieTitle = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
        String movieReleaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
        float movieVoteAverage = cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));
        String moviePlotSynopsis = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS));
        String moviePoster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
        String movieBackdropPhoto = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PHOTO_PATH));
        int movieFavorite = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE));
        boolean isMovieFavorite = movieFavorite == 1;

        return new Movie(movieId, movieTitle, movieReleaseDate, movieVoteAverage, moviePlotSynopsis, moviePoster, movieBackdropPhoto, isMovieFavorite);
    }
}
