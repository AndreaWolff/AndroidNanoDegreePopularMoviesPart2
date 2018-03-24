package com.andrea.popularmoviespart2.features.details.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.details.DetailsContract;

import java.util.Locale;

import javax.inject.Inject;

import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE;

public class DetailsPresenter {

    private final Context context;

    private DetailsContract.View view;
    private Movie movie;

    @Inject
    DetailsPresenter(@NonNull Context context) {
        this.context = context;
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
}
