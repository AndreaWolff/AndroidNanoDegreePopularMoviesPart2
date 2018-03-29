package com.andrea.popularmoviespart2.features.details;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.domain.MovieReview;

import java.util.List;

public interface DetailsContract {
    interface View {
        void renderScreenTitle(@NonNull String title);

        void renderMovieTitle(@NonNull String title);

        void renderReleaseDate(@NonNull String releaseDate);

        void renderVoteAverage(@NonNull String voteAverage, float rating);

        void renderPlotSynopsis(@NonNull String plotSynopsis);

        void renderPosterImage(@NonNull String posterPath);

        void renderBackdropPhoto(@NonNull String backdropPhotoPath);

        void finishActivity();

        void getMovieTrailerIntent(@NonNull Intent appIntent, @NonNull Intent webIntent);

        void showError(@NonNull String errorTitle, @NonNull String errorMessage);

        void showProgressBar();

        void hideProgressBar();

        void showMovieReviews(@NonNull List<MovieReview> movieReviews);

        void renderReviewLabel(@NonNull String label);

        void setFavoriteButton(@NonNull Drawable drawable);

        void shareYouTubeTrailer(@NonNull String type, @NonNull String youTubeTrailer);
    }
}
