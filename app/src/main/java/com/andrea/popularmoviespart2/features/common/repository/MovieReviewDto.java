package com.andrea.popularmoviespart2.features.common.repository;

import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.google.gson.annotations.SerializedName;

class MovieReviewDto {

    @SerializedName("author") private String author;
    @SerializedName("content") private String content;
    @SerializedName("id") private String id;
    @SerializedName("url") private String url;

    MovieReview toMovieReview() {
        return new MovieReview(author,
                content,
                id,
                url);
    }
}