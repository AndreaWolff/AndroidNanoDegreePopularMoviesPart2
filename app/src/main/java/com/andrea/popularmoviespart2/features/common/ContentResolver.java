package com.andrea.popularmoviespart2.features.common;

import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.domain.Movie;

import io.reactivex.Single;

public interface ContentResolver {

    Single<Boolean> getFavoriteMovie(@NonNull String movieId);

    void insertFavoriteMovie(@NonNull Movie movie);

    void updateFavoriteMovie(@NonNull Movie movie);
}
