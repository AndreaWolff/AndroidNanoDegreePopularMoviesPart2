package com.andrea.popularmoviespart2.features.main;


import android.content.Intent;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.domain.Movie;

import java.util.List;

public interface MainContract {
    interface View {
        void renderPopularMoviesTitle(@NonNull String title);

        void renderTopRatedMoviesTitle(@NonNull String title);

        void showMoviesList(@NonNull List<Movie> movieList);

        void showError(@NonNull String title, @NonNull String errorMessage);

        void showProgressBar();

        void hideProgressBar();

        void hideProgressBarOnMovieListError();

        void navigateToMovieDetails(@NonNull Intent intent);
    }
}
