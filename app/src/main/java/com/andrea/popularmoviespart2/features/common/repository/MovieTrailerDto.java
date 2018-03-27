package com.andrea.popularmoviespart2.features.common.repository;

import com.andrea.popularmoviespart2.features.common.domain.MovieTrailer;
import com.google.gson.annotations.SerializedName;

class MovieTrailerDto {

    @SerializedName("id") private String id;
    @SerializedName("key") private String key;
    @SerializedName("name") private String name;
    @SerializedName("site") private String site;
    @SerializedName("type") private String type;

    MovieTrailer toMovieTrailer() {
        return new MovieTrailer(id,
                                key,
                                name,
                                site,
                                type);
    }
}
