package com.andrea.popularmoviespart2.features.common.repository;


import io.reactivex.Single;
import retrofit2.http.GET;

import static com.andrea.popularmoviespart2.features.common.ActivityConstants.POPULAR_MOVIES;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.TOP_RATED_MOVIES;

public interface MovieDao {

    @GET(POPULAR_MOVIES) Single<MoviesDto> getPopularMoviesList();

    @GET(TOP_RATED_MOVIES) Single<MoviesDto> getTopRatedMoviesList();

}
