package com.andrea.popularmoviespart2.features.common.repository;

import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class MovieReviewsDto {

    @SerializedName("results") private List<MovieReviewDto> movieReviews;

    List<MovieReview> toReviews() {
        List<MovieReview>  result = new ArrayList<>();

        for (MovieReviewDto dto : movieReviews) {
            result.add(dto.toMovieReview());
        }

        return result;
    }
}
