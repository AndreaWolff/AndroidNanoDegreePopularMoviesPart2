package com.andrea.popularmoviespart2.features.details.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.application.MovieApplication;
import com.andrea.popularmoviespart2.dagger.component.DaggerDetailsComponent;
import com.andrea.popularmoviespart2.databinding.ActivityDetailsBinding;
import com.andrea.popularmoviespart2.features.details.DetailsContract;
import com.andrea.popularmoviespart2.features.details.logic.DetailsPresenter;
import com.andrea.popularmoviespart2.util.GlideUtil;

import javax.inject.Inject;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View {

    ActivityDetailsBinding binding;

    @Inject
    DetailsPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        DaggerDetailsComponent.builder()
                .appComponent(MovieApplication.getDagger())
                .build()
                .inject(this);

        presenter.connectView(this, getIntent());
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.disconnectView();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // region View methods
    @Override public void renderScreenTitle(@NonNull String title) {
        setTitle(title);
    }

    @Override public void renderMovieTitle(@NonNull String title) {
        binding.detailsMovieTitleTextView.setText(title);
    }

    @Override public void renderReleaseDate(@NonNull String releaseDate) {
        binding.detailsReleaseDateTextView.setText(releaseDate);
    }

    @Override public void renderVoteAverage(@NonNull String voteAverage, float rating) {
        binding.detailsMovieRatingBar.setRating(rating);
        binding.detailsMovieRatingTextView.setText(voteAverage);
    }

    @Override public void renderPlotSynopsis(@NonNull String plotSynopsis) {
        binding.detailsPlotSynopsisTextView.setText(plotSynopsis);
    }

    @Override public void renderPosterImage(@NonNull String posterPath) {
        GlideUtil.displayImage(posterPath, binding.detailsPosterImageView);
    }

    @Override public void renderBackdropPhoto(@NonNull String backdropPhotoPath) {
        GlideUtil.displayImage(backdropPhotoPath, binding.detailsBackdropImageView);
    }

    @Override public void finishActivity() {
        finish();
    }
    // endregion
}
