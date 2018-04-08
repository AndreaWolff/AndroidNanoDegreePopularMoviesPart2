package com.andrea.popularmoviespart2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PHOTO_PATH;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.TABLE_NAME;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                COLUMN_MOVIE_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
                COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                COLUMN_MOVIE_BACKDROP_PHOTO_PATH + " TEXT NOT NULL, " +
                COLUMN_MOVIE_FAVORITE + " INTEGER default 0, " +
                " UNIQUE (" + COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Alter the table rather than Dropping the table, so ensure that data is not lost.
        sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}