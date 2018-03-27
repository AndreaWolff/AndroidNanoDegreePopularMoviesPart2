package com.andrea.popularmoviespart2.features.details.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.andrea.popularmoviespart2.features.common.domain.MovieTrailer;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.details.DetailsContract;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static com.andrea.popularmoviespart2.features.common.ActivityConstants.ERROR_MESSAGE_LOGGER;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE;

public class DetailsPresenter {

    private final Context context;
    private final MovieRepository movieRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private DetailsContract.View view;
    private Movie movie;

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
        if (view != null) {
            view.showProgressBar();
        }

        disposable.add(movieRepository.getMovieReviews(movie.getId())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(this::handleMovieReviewsResponseSuccessful, this::handleResponseError));
    }

    private void handleMovieReviewsResponseSuccessful(List<MovieReview> movieReviews) {
        if (view != null) {
            view.hideProgressBar();

            if (movieReviews.isEmpty()) {
                view.renderReviewLabel(R.string.details_no_reviews_label);
                return;
            }

            view.renderReviewLabel(context.getString(R.string.details_review_label));
            view.showMovieReviews(movieReviews);
        }
    }

    public void watchTrailerSelected() {
        disposable.add(movieRepository.getMovieTrailers(movie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleMovieTrailersResponseSuccessful, this::handleResponseError));
    }

    private void handleMovieTrailersResponseSuccessful(List<MovieTrailer> movieTrailers) {
        if (view != null) {
            MovieTrailer movieTrailer = movieTrailers.get(new Random().nextInt(movieTrailers.size()));

            // Taken from https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
            Intent appIntent = new Intent(Intent.ACTION_VIEW, movieTrailer.getYouTubeTrailerAppUrl());
            Intent webIntent = new Intent(Intent.ACTION_VIEW, movieTrailer.getYouTubeTrailerWebUrl());

            view.getMovieTrailerIntent(appIntent, webIntent);
        }
    }

    private void handleResponseError(Throwable error) {
        if (view != null) {
            view.hideProgressBar();
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
}
