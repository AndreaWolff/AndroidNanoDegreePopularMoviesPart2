package com.andrea.popularmoviespart2.features.common;

import android.content.AsyncQueryHandler;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.domain.Movie;

public interface ContentResolver {

    void getFavoriteMovie(@NonNull AsyncQueryHandler queryHandler, @NonNull String movieId);

    void insertFavoriteMovie(@NonNull Movie movie);

    void updateFavoriteMovie(@NonNull Movie movie);
}
