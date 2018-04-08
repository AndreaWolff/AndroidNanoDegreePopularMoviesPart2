package com.andrea.popularmoviespart2.features.common;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.data.MovieContract;
import com.andrea.popularmoviespart2.features.common.domain.Movie;

import javax.inject.Inject;

import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PHOTO_PATH;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.buildMovieId;

public class ContentResolverDefault implements ContentResolver {

    private final Context context;

    @Inject
    ContentResolverDefault(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getFavoriteMovie(@NonNull AsyncQueryHandler queryHandler, @NonNull String movieId) {
        queryHandler.startQuery(1,
                                null,
                                CONTENT_URI,
                                null,
                                COLUMN_MOVIE_ID + " = ? AND " + COLUMN_MOVIE_FAVORITE + " = ?",
                                new String[]{movieId, "1"},
                                null);
    }

    @Override
    public void insertFavoriteMovie(@NonNull Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(COLUMN_MOVIE_PLOT_SYNOPSIS, movie.getPlotSynopsis());
        contentValues.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(COLUMN_MOVIE_BACKDROP_PHOTO_PATH, movie.getBackdropPhotoPath());
        contentValues.put(COLUMN_MOVIE_FAVORITE, 1);

        context.getContentResolver().insert(CONTENT_URI, contentValues);
    }

    @Override
    public void updateFavoriteMovie(@NonNull Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_FAVORITE, 0);
        context.getContentResolver().update(buildMovieId(movie.getId()),
                contentValues,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
    }
}
