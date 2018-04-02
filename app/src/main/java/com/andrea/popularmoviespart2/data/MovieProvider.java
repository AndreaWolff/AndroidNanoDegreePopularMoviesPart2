package com.andrea.popularmoviespart2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.content.UriMatcher.NO_MATCH;
import static com.andrea.popularmoviespart2.data.MovieContract.CONTENT_AUTHORITY;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.TABLE_NAME;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.buildMovieUri;
import static com.andrea.popularmoviespart2.data.MovieContract.PATH_MOVIE;

public class MovieProvider extends ContentProvider {

    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private MovieDbHelper movieDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, MOVIE);
        matcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case MOVIE:
                cursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() == null) {
            return null;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case MOVIE:
                long id = db.insert(TABLE_NAME, null, contentValues);

                if (id > 0) {
                    returnUri = buildMovieUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() == null) {
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int tasksUpdated;
        int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_ID:
                tasksUpdated = db.update(TABLE_NAME,
                                         values,
                                         selection,
                                         selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() == null) {
            return -1;
        }

        if (tasksUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksUpdated;
    }
}