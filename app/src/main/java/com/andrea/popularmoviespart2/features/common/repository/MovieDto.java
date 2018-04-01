package com.andrea.popularmoviespart2.features.common.repository;

import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.google.gson.annotations.SerializedName;

class MovieDto {

    @SerializedName("id") private String id;
    @SerializedName("title") private String title;
    @SerializedName("release_date") private String releaseDate;
    @SerializedName("vote_average") private float voteAverage;
    @SerializedName("overview") private String plotSynopsis;
    @SerializedName("poster_path") private String posterPath;
    @SerializedName("backdrop_path") private String backdropPhotoPath;

    Movie toMovie() {
        return new Movie(id,
                title,
                releaseDate,
                voteAverage,
                plotSynopsis,
                posterPath,
                backdropPhotoPath,
                false);
    }
}
