package com.andrea.popularmoviespart2.features.common.repository;


import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.domain.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class MovieRepositoryDefault implements MovieRepository {

    private final MovieDao movieDao;

    private List<Movie> cachedPopularMovies;
    private List<Movie> cachedTopRatedMovies;

    @Inject
    MovieRepositoryDefault(@NonNull MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @NonNull @Override public Single<List<Movie>> getPopularMovies() {
        if (cachedPopularMovies != null) {
            return Single.just(cachedPopularMovies);
        }

        return movieDao.getPopularMoviesList().map(moviesDto -> {
            cachedPopularMovies = moviesDto.toMovies();
            return cachedPopularMovies;
        });
    }

    @NonNull @Override public Single<List<Movie>> getTopRatedMovies() {
        if (cachedTopRatedMovies != null) {
            return Single.just(cachedTopRatedMovies);
        }

        return movieDao.getTopRatedMoviesList().map(moviesDto -> {
            cachedTopRatedMovies = moviesDto.toMovies();
            return cachedTopRatedMovies;
        });
    }
}
