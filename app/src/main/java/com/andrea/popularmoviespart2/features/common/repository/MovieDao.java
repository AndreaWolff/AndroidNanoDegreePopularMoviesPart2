package com.andrea.popularmoviespart2.features.common.repository;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.andrea.popularmoviespart2.features.common.ActivityConstants.API_KEY;

public interface MovieDao {

    @GET("movie/popular?api_key=" + API_KEY) Single<MoviesDto> getPopularMoviesList();

    @GET("movie/top_rated?api_key=" + API_KEY) Single<MoviesDto> getTopRatedMoviesList();

    @GET("movie/{id}/videos?api_key=" + API_KEY) Single<MovieTrailersDto> getMovieTrailers(@Path("id") String id);

    @GET("movie/{id}/reviews?api_key=" + API_KEY) Single<MovieReviewsDto> getMovieReviews(@Path("id") String id);

}
