package com.andrea.popularmoviespart2.features.main.logic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.data.MovieContract;
import com.andrea.popularmoviespart2.features.common.ServerError;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.details.ui.DetailsActivity;
import com.andrea.popularmoviespart2.features.main.MainContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.ERROR_MESSAGE_LOGGER;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE_LOADER_ID;

public class MainPresenter {

    private static final String POPULAR_MOVIE_LIST_SHOWN = "MPPMLS";
    private static final String TOP_RATED_MOVIE_LIST_SHOWN = "MPTRMLS";
    private static final String FAVORITE_MOVIE_LIST_SHOWN = "MPFMLS";

    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MovieRepository movieRepository;
    private final Context context;

    private MainContract.View view;
    private boolean isTopRatedMovies;
    private boolean isPopularMovies;
    private boolean isFavoriteMovies;

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

            if (savedInstanceState.getBoolean(FAVORITE_MOVIE_LIST_SHOWN)) {
                loadFavoriteMovies();
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
        outState.putBoolean(FAVORITE_MOVIE_LIST_SHOWN, isFavoriteMovies);
    }

    public void disconnectView() {
        disposable.clear();
        view = null;
    }

    public void loadPopularMovies() {
        if (view != null) {
            view.showProgressBar();
            view.renderPopularMoviesTitle(context.getString(R.string.main_popular_movies_title));
        }

        disposable.add(movieRepository.getPopularMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlePopularMoviesResponseSuccessful, this::handleResponseError));
    }

    public void loadTopRatedMovies() {
        if (view != null) {
            view.showProgressBar();
            view.renderTopRatedMoviesTitle(context.getString(R.string.main_top_rated_movies_title));
        }

        disposable.add(movieRepository.getTopRatedMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleTopRatedMoviesResponseSuccessful, this::handleResponseError));
    }

    public void onMoviePosterSelected(@NonNull Movie movie) {
        if (view != null) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(MOVIE, movie);
            view.navigateToMovieDetails(intent);
        }
    }

    // region Favorite Movies
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case MOVIE_LOADER_ID:
                return new CursorLoader(context,
                                        CONTENT_URI,
                                        null,
                                        MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE + " = ?",
                                        new String[]{"1"},
                                        null
                );
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    public void onLoadFinished(@NonNull Cursor data) {
        if (view != null) {
            view.renderPopularMoviesTitle(context.getString(R.string.main_favorite_movies_title));
            view.swapCursor(data);

            if (data.getCount() > 0) {
                view.showFavoriteMoviesList();
            } else {
                view.showError("", context.getString(R.string.error_no_favorite_movies));
                view.hideProgressBarOnMovieListError();
            }
        }
    }

    public void onLoaderReset() {
        if (view != null) {
            view.swapCursor(null);
        }
    }

    public void loadFavoriteMovies() {
        configureMovieListOrigins(false, false, true);

        if (view != null) {
            view.hideProgressBar();
            view.configureFavoriteMoviesAdapter();
            view.configureFavoriteMovieLoader(MOVIE_LOADER_ID);
        }
    }
    // endregion

    public void swipeToRefresh() {
        if (isPopularMovies) {
            disposable.add(movieRepository.getRefreshedPopularMovies()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleRefreshedMoviesResponseSuccessful, this::handleRefreshMoviesResponseError));
            return;
        }

        if (isTopRatedMovies) {
            disposable.add(movieRepository.getRefreshedTopRatedMovies()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleRefreshedMoviesResponseSuccessful, this::handleRefreshMoviesResponseError));
            return;
        }

        if (isFavoriteMovies) {
            if (view != null) {
                view.hideSwipeToRefresh();
            }
        }
    }

    // region Movie Lists
    private void handlePopularMoviesResponseSuccessful(List<Movie> movies) {
        configureMovieListOrigins(true, false, false);

        if (view != null) {
            view.hideProgressBar();
            view.hideSwipeToRefresh();
            view.showMoviesList(movies);
        }
    }

    private void handleTopRatedMoviesResponseSuccessful(List<Movie> movies) {
        configureMovieListOrigins(false, true, false);

        if (view != null) {
            view.hideProgressBar();
            view.hideSwipeToRefresh();
            view.showMoviesList(movies);
        }
    }

    private void handleRefreshedMoviesResponseSuccessful(List<Movie> movies) {
        if (view != null) {
            view.hideSwipeToRefresh();
            view.showMoviesList(movies);
        }
    }

    private void handleRefreshMoviesResponseError(Throwable error) {
        if (view != null) {
            view.hideSwipeToRefresh();

            configureErrorMessage(error);

            if (isPopularMovies) {
                List<Movie> cachedPopularMovies = movieRepository.getCachedPopularMovies();
                if (cachedPopularMovies != null) {
                    view.showMoviesList(cachedPopularMovies);
                }
                return;
            }

            if (isTopRatedMovies) {
                List<Movie> cachedTopRatedMovies = movieRepository.getCachedTopRatedMovies();
                if (cachedTopRatedMovies != null) {
                    view.showMoviesList(cachedTopRatedMovies);
                }
            }
        }
    }

    private void handleResponseError(Throwable error) {
        if (view != null) {
            view.hideProgressBarOnMovieListError();
            view.hideSwipeToRefresh();
        }

        configureErrorMessage(error);
    }

    private void configureErrorMessage(Throwable error) {
        String errorTitle;
        String errorMessage;
        String reasonForError = ServerError.getReasonForError(error);

        switch (reasonForError) {
            case "unauthorizedError":
                errorTitle = context.getString(R.string.error_title_unauthorized);
                errorMessage = context.getString(R.string.error_message_unauthorized);
                break;
            case "timeoutError":
                errorTitle = context.getString(R.string.error_title_timeout);
                errorMessage = context.getString(R.string.error_message_timeout);
                break;
            case "noHostError":
                errorTitle = context.getString(R.string.error_title_no_resolved_host);
                errorMessage = context.getString(R.string.error_message_no_resolved_host);
                break;
            case "defaultError":
                errorTitle = context.getString(R.string.error_title);
                errorMessage = context.getString(R.string.error_message);
                break;
            default:
                errorTitle = context.getString(R.string.error_title);
                errorMessage = context.getString(R.string.error_message);
                Log.d(ERROR_MESSAGE_LOGGER, error.getMessage());
                break;
        }

        if (view != null) {
            view.showError(errorTitle, errorMessage);
        }
    }
    // endregion

    private void configureMovieListOrigins(boolean isPopularMovies, boolean isTopRatedMovies, boolean isFavoriteMovies) {
        this.isPopularMovies = isPopularMovies;
        this.isTopRatedMovies = isTopRatedMovies;
        this.isFavoriteMovies = isFavoriteMovies;
    }
}
