package com.andrea.popularmoviespart2.features.common.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.andrea.popularmoviespart2.features.common.domain.MovieTrailer;

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

    @NonNull @Override public Single<List<MovieTrailer>> getMovieTrailers(@NonNull String id) {
        return movieDao.getMovieTrailers(id).map(MovieTrailersDto::toTrailers);
    }

    @NonNull @Override public Single<List<MovieReview>> getMovieReviews(@NonNull String id) {
        return movieDao.getMovieReviews(id).map(MovieReviewsDto::toReviews);
    }

    @NonNull @Override public Single<List<Movie>> getRefreshedPopularMovies() {
        return movieDao.getPopularMoviesList().map(MoviesDto::toMovies);
    }

    @NonNull @Override public Single<List<Movie>> getRefreshedTopRatedMovies() {
        return movieDao.getTopRatedMoviesList().map(MoviesDto::toMovies);
    }

    @Nullable @Override public List<Movie> getCachedPopularMovies() {
        return cachedPopularMovies;
    }

    @Nullable @Override public List<Movie> getCachedTopRatedMovies() {
        return cachedTopRatedMovies;
    }
}
