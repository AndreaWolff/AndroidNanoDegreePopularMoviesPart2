package com.andrea.popularmoviespart2.features.common;


import com.andrea.popularmoviespart2.BuildConfig;

public class ActivityConstants {
    public static final String MOVIE = "M";
    public static final String ERROR_MESSAGE_LOGGER = "Error message:";

    private static final String API_KEY = BuildConfig.API_KEY;
    public static final String POPULAR_MOVIES = "movie/popular?api_key=" + API_KEY;
    public static final String TOP_RATED_MOVIES = "movie/top_rated?api_key=" + API_KEY;
}
