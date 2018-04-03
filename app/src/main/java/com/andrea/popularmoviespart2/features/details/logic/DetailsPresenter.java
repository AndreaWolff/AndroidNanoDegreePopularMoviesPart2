package com.andrea.popularmoviespart2.features.details.logic;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.data.MovieContract;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.andrea.popularmoviespart2.features.common.domain.MovieTrailer;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.details.DetailsContract;
import com.andrea.popularmoviespart2.util.DrawableUtil;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PHOTO_PATH;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.andrea.popularmoviespart2.data.MovieContract.MovieEntry.buildMovieId;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.ERROR_MESSAGE_LOGGER;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE;

public class DetailsPresenter {

    private final Context context;
    private final MovieRepository movieRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private DetailsContract.View view;
    private Movie movie;
    private MovieTrailer movieTrailer;
    private List<MovieReview> movieReviews;

    private boolean requestInProgress;
    private boolean isFavorite;

    @Inject
    DetailsPresenter(@NonNull Context context,
                     @NonNull MovieRepository movieRepository) {
        this.context = context;
        this.movieRepository = movieRepository;
    }

    public void connectView(@NonNull DetailsContract.View view, @NonNull Intent intent) {
        this.view = view;

        Bundle extras = intent.getExtras();
        if (extras == null) {
            view.finishActivity();
            return;
        }

        movie = extras.getParcelable(MOVIE);

        init();
    }

    private void init() {
        if (view != null) {
            view.renderScreenTitle(context.getString(R.string.details_movie_title));
        }

        isFavorite = movie.isFavorite();
        refreshUI();

        populateDetails(movie.getTitle(),
                movie.getReleaseDate(),
                movie.getVoteAverage(),
                movie.getPlotSynopsis(),
                movie.getPosterPath(),
                movie.getBackdropPhotoPath());
    }

    private void populateDetails(@NonNull String title,
                                 @NonNull String releaseDate,
                                 float voteAverage,
                                 @NonNull String plotSynopsis,
                                 @NonNull String posterPath,
                                 @NonNull String backdropPhotoPath) {
        if (view != null) {
            view.renderMovieTitle(title);
            view.renderReleaseDate(releaseDate);

            float starRating = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f", voteAverage));
            float voteAvg = voteAverage * 10;
            view.renderVoteAverage((int) voteAvg + "%", starRating / 2);

            view.renderPlotSynopsis(plotSynopsis);
            view.renderPosterImage(posterPath);
            view.renderBackdropPhoto(backdropPhotoPath);
        }
    }

    public void disconnectView() {
        view = null;
    }

    public void onResume() {
        if (movieReviews == null) {
            loadMovieReviews();
        } else {
            configureMovieReviews(movieReviews);
        }

        if (movieTrailer == null) {
            loadMovieTrailers();
        }
    }

    private void loadMovieReviews() {
        disposable.add(movieRepository.getMovieReviews(movie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleMovieReviewsResponseSuccessful, this::handleResponseError));
    }

    public void favoriteSelected() {
        ContentValues contentValues = new ContentValues();

        if (!isFavorite) {
            contentValues.put(COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(COLUMN_MOVIE_TITLE, movie.getTitle());
            contentValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
            contentValues.put(COLUMN_MOVIE_PLOT_SYNOPSIS, movie.getPlotSynopsis());
            contentValues.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
            contentValues.put(COLUMN_MOVIE_BACKDROP_PHOTO_PATH, movie.getBackdropPhotoPath());
            contentValues.put(COLUMN_MOVIE_FAVORITE, 1);

            context.getContentResolver().insert(CONTENT_URI, contentValues);

            isFavorite = true;
            refreshUI();
            return;
        }

        contentValues.put(COLUMN_MOVIE_FAVORITE, 0);
        context.getContentResolver().update(buildMovieId(movie.getId()),
                                            contentValues,
                                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                            new String[]{String.valueOf(movie.getId())});

        isFavorite = false;
        refreshUI();
    }

    public void shareYouTubeTrailer() {
        if (view != null && movieTrailer != null) {
            view.shareYouTubeTrailer("text/plain",
                                      movieTrailer.getYouTubeTrailerWebUrl().toString());
        }
    }

    public void watchTrailerSelected() {
        if (movieTrailer == null) {
            return;
        }

        configureMovieTrailer();
    }

    private void loadMovieTrailers() {
        if (requestInProgress) {
            return;
        }

        requestInProgress = true;
        refreshUI();

        disposable.add(movieRepository.getMovieTrailers(movie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleMovieTrailersResponseSuccessful, this::handleResponseError));
    }

    private void handleMovieReviewsResponseSuccessful(List<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
        configureMovieReviews(movieReviews);
    }

    private void configureMovieReviews(List<MovieReview> movieReviews) {
        refreshUI();

        if (view != null) {
            if (movieReviews.isEmpty()) {
                view.renderReviewLabel(context.getString(R.string.details_no_reviews_label));
                return;
            }

            view.renderReviewLabel(context.getString(R.string.details_reviews_label));
            view.showMovieReviews(movieReviews);
        }
    }

    private void handleMovieTrailersResponseSuccessful(List<MovieTrailer> movieTrailers) {
        requestInProgress = false;
        refreshUI();

        if (view != null) {
            if (movieTrailers.isEmpty()) {
                view.hideMovieTrailerButtons();
                return;
            }

            view.showMovieTrailerButtons();
        }
        movieTrailer = movieTrailers.get(new Random().nextInt(movieTrailers.size()));
    }

    private void configureMovieTrailer() {
        // Taken from https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
        Intent appIntent = new Intent(Intent.ACTION_VIEW, movieTrailer.getYouTubeTrailerAppUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, movieTrailer.getYouTubeTrailerWebUrl());

        if (view != null) {
            view.getMovieTrailerIntent(appIntent, webIntent);
        }
    }

    private void handleResponseError(Throwable error) {
        requestInProgress = false;

        refreshUI();
        configureErrorMessage(error);
    }

    private void configureErrorMessage(Throwable error) {
        String errorTitle;
        String errorMessage;

        if (error.getCause() == null) {
            errorTitle = context.getString(R.string.error_title);
            errorMessage = context.getString(R.string.error_message);
            configureErrorDialog(errorTitle, errorMessage);
            return;
        }

        if (error.getMessage() == null) {
            errorTitle = context.getString(R.string.error_title);
            errorMessage = context.getString(R.string.error_message);
            configureErrorDialog(errorTitle, errorMessage);
            return;
        }

        switch (error.getMessage()) {
            case "HTTP 401 Unauthorized":
                errorTitle = context.getString(R.string.error_title_unauthorized);
                errorMessage = context.getString(R.string.error_message_unauthorized);
                break;
            case "timeout":
                errorTitle = context.getString(R.string.error_title_timeout);
                errorMessage = context.getString(R.string.error_message_timeout);
                break;
            case "Unable to resolve host \"api.themoviedb.org\": No address associated with hostname":
                errorTitle = context.getString(R.string.error_title_no_resolved_host);
                errorMessage = context.getString(R.string.error_message_no_resolved_host);
                break;
            default:
                errorTitle = context.getString(R.string.error_title);
                errorMessage = context.getString(R.string.error_message);
                Log.d(ERROR_MESSAGE_LOGGER, error.getMessage());
                break;
        }

        configureErrorDialog(errorTitle, errorMessage);
    }

    private void configureErrorDialog(String errorTitle, String errorMessage) {
        if (view != null) {
            view.showError(errorTitle, errorMessage);
        }
    }

    private void refreshUI() {
        if (view != null) {
            if (requestInProgress) {
                view.showContentProgressBar();
                view.showProgressBar();
            } else {
                view.hideContentProgressBar();
                view.hideProgressBar();
            }

            if (isFavorite) {
                view.setFavoriteButton(DrawableUtil.getTintedDrawable(context, R.drawable.icon_favorite, R.color.colorAccent));
            } else {
                view.setFavoriteButton(DrawableUtil.getTintedDrawable(context, R.drawable.icon_favorite, R.color.white));
            }
        }
    }
}
