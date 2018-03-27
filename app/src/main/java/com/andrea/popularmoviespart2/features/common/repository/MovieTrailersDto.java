package com.andrea.popularmoviespart2.features.common.repository;

import com.andrea.popularmoviespart2.features.common.domain.MovieTrailer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class MovieTrailersDto {

    @SerializedName("results") private List<MovieTrailerDto> movieTrailer;

    List<MovieTrailer> toTrailers() {
        List<MovieTrailer>  result = new ArrayList<>();

        for (MovieTrailerDto dto : movieTrailer) {
            result.add(dto.toMovieTrailer());
        }

        return result;
    }
}
