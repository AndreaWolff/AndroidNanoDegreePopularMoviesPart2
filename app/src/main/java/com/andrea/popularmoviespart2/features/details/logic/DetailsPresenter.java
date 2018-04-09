package com.andrea.popularmoviespart2.features.details.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.ContentResolver;
import com.andrea.popularmoviespart2.features.common.ServerError;
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

import static com.andrea.popularmoviespart2.features.common.ActivityConstants.ERROR_MESSAGE_LOGGER;
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE;

public class DetailsPresenter {

    private final Context context;
    private final MovieRepository movieRepository;
    private final ContentResolver contentResolver;
    private final CompositeDisposable disposable = new CompositeDisposable();

    private DetailsContract.View view;
    private Movie movie;
    private List<MovieTrailer> movieTrailers;
    private MovieTrailer movieTrailer;
    private List<MovieReview> movieReviews;

    private boolean requestInProgress;
    private boolean isFavorite;

    @Inject
    DetailsPresenter(@NonNull Context context,
                     @NonNull MovieRepository movieRepository,
                     @NonNull ContentResolver contentResolver) {
        this.context = context;
        this.movieRepository = movieRepository;
        this.contentResolver = contentResolver;
    }

    public void connectView(@NonNull DetailsContract.View view, @Nullable Bundle extras) {
        this.view = view;

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

        if (!isFavorite) {
            disposable.add(contentResolver.getFavoriteMovie(movie.getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleGetFavoriteResponseSuccessful, this::handleResponseError));
        }

        refreshUI();

        populateDetails(movie.getTitle(),
                movie.getReleaseDate(),
                movie.getVoteAverage(),
                movie.getPlotSynopsis(),
                movie.getPosterPath(),
                movie.getBackdropPhotoPath());
    }

    private void handleGetFavoriteResponseSuccessful(Boolean isFavorite) {
        this.isFavorite = isFavorite;
        refreshUI();
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

        if (movieTrailers == null) {
            loadMovieTrailers();
        }
    }

    public void favoriteSelected() {
        if (!isFavorite) {
            contentResolver.insertFavoriteMovie(movie);

            isFavorite = true;
            refreshUI();
            return;
        }

        contentResolver.updateFavoriteMovie(movie);

        isFavorite = false;
        refreshUI();
    }

    public void shareYouTubeTrailer() {
        if (view != null && movieTrailers != null) {
            movieTrailer = movieTrailers.get(new Random().nextInt(movieTrailers.size()));
            view.shareYouTubeTrailer("text/plain", movieTrailer.getYouTubeTrailerWebUrl().toString());
        }
    }

    public void watchTrailerSelected() {
        if (movieTrailers == null) {
            return;
        }

        configureMovieTrailer();
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

    private void loadMovieReviews() {
        disposable.add(movieRepository.getMovieReviews(movie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleMovieReviewsResponseSuccessful, this::handleResponseError));
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
        this.movieTrailers = movieTrailers;
    }

    private void configureMovieTrailer() {
        if (movieTrailer == null) {
            movieTrailer = movieTrailers.get(new Random().nextInt(movieTrailers.size()));
        }

        // Taken from https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
        Intent appIntent = new Intent(Intent.ACTION_VIEW, movieTrailer.getYouTubeTrailerAppUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, movieTrailer.getYouTubeTrailerWebUrl());

        if (view != null) {
            view.getMovieTrailerIntent(appIntent, webIntent);
        }
    }

    private void handleResponseError(Throwable error) {
        requestInProgress = false;
        isFavorite = false;

        refreshUI();
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
                view.setFavoriteButton(DrawableUtil.getTintedDrawable(context, R.drawable.icon_favorite, R.color.divider));
            }
        }
    }
}
