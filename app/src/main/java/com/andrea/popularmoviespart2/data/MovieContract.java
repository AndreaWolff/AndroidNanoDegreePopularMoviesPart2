package com.andrea.popularmoviespart2.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.andrea.popularmoviespart2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String COLUMN_MOVIE_PLOT_SYNOPSIS = "movie_plot_synopsis";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String COLUMN_MOVIE_BACKDROP_PHOTO_PATH = "movie_backdrop_photo_path";
        public static final String COLUMN_MOVIE_FAVORITE = "movie_favorite";

        @NonNull public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        @NonNull public static Uri buildMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }
}