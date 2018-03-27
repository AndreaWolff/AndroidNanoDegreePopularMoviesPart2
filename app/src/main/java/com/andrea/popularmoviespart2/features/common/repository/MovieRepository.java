package com.andrea.popularmoviespart2.features.common.repository;


import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.andrea.popularmoviespart2.features.common.domain.MovieTrailer;

import java.util.List;

import io.reactivex.Single;

public interface MovieRepository {

    @NonNull Single<List<Movie>> getPopularMovies();

    @NonNull Single<List<Movie>> getTopRatedMovies();

    @NonNull Single<List<MovieTrailer>> getMovieTrailers(@NonNull String id);

    @NonNull Single<List<MovieReview>> getMovieReviews(@NonNull String id);

}
