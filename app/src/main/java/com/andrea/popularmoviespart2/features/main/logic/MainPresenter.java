package com.andrea.popularmoviespart2.features.main.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.details.ui.DetailsActivity;
import com.andrea.popularmoviespart2.features.main.MainContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.andrea.popularmoviespart2.features.common.ActivityConstants.ERROR_MESSAGE_LOGGER;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE;

public class MainPresenter {

    private static final String POPULAR_MOVIE_LIST_SHOWN = "MPPMLS";
    private static final String TOP_RATED_MOVIE_LIST_SHOWN = "MPTRMLS";

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MovieRepository movieRepository;
    private final Context context;

    private MainContract.View view;
    private boolean isTopRatedMovies;
    private boolean isPopularMovies;

    @Inject
    MainPresenter(@NonNull MovieRepository movieRepository,
                  @NonNull Context context) {
        this.movieRepository = movieRepository;
        this.context = context;
    }

    public void connectView(@NonNull MainContract.View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        init();

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(POPULAR_MOVIE_LIST_SHOWN)) {
                loadPopularMovies();
                return;
            }

            if (savedInstanceState.getBoolean(TOP_RATED_MOVIE_LIST_SHOWN)) {
                loadTopRatedMovies();
                return;
            }
        }

        loadPopularMovies();

    }

    private void init() {

        if (view != null) {
            view.renderPopularMoviesTitle(context.getString(R.string.main_popular_movies_title));
        }
    }

    public void onSavedInstanceState(Bundle outState) {
        outState.putBoolean(POPULAR_MOVIE_LIST_SHOWN, isPopularMovies);
        outState.putBoolean(TOP_RATED_MOVIE_LIST_SHOWN, isTopRatedMovies);
    }

    public void disconnectView() {
        disposable.clear();
        view = null;
    }

    public void loadPopularMovies() {

        if (view != null) {
            view.showProgressBar();
        }

        disposable.add(movieRepository.getPopularMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlePopularMoviesResponseSuccessful, this::handleResponseError));
    }

    public void loadTopRatedMovies() {

        if (view != null) {
            view.showProgressBar();
        }

        disposable.add(movieRepository.getTopRatedMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleTopRatedMoviesResponseSuccessful, this::handleResponseError));
    }

    private void handlePopularMoviesResponseSuccessful(List<Movie> movies) {
        isPopularMovies = true;
        isTopRatedMovies = false;

        if (view != null) {
            view.hideProgressBar();
            view.renderPopularMoviesTitle(context.getString(R.string.main_popular_movies_title));
            view.showMoviesList(movies);
        }
    }

    private void handleTopRatedMoviesResponseSuccessful(List<Movie> movies) {
        isTopRatedMovies = true;
        isPopularMovies = false;

        if (view != null) {
            view.hideProgressBar();
            view.renderTopRatedMoviesTitle(context.getString(R.string.main_top_rated_movies_title));
            view.showMoviesList(movies);
        }
    }

    private void handleResponseError(Throwable error) {

        if (view != null) {
            view.hideProgressBarOnMovieListError();

            configureErrorMessage(error);
        }
    }

    private void configureErrorMessage(Throwable error) {
        String errorTitle;
        String errorMessage;

        if (error.getMessage().equals("HTTP 401 Unauthorized")) {
            errorTitle = context.getString(R.string.error_title_unauthorized);
            errorMessage = context.getString(R.string.error_message_unauthorized);
        } else if (error.getMessage().equals("timeout")) {
            errorTitle = context.getString(R.string.error_title_timeout);
            errorMessage = context.getString(R.string.error_message_timeout);
        } else {
            errorTitle = context.getString(R.string.error_title);
            errorMessage = context.getString(R.string.error_message);
            Log.d(ERROR_MESSAGE_LOGGER, error.getMessage());
        }

        view.showError(errorTitle, errorMessage);
    }

    public void onMoviePosterSelected(Movie movie) {

        if (view != null) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(MOVIE, movie);
            view.navigateToMovieDetails(intent);
        }
    }
}
