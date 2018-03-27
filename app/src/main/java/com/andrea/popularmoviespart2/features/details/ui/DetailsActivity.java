package com.andrea.popularmoviespart2.features.details.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.application.MovieApplication;
import com.andrea.popularmoviespart2.dagger.component.DaggerDetailsComponent;
import com.andrea.popularmoviespart2.databinding.ActivityDetailsBinding;
import com.andrea.popularmoviespart2.features.common.domain.MovieReview;
import com.andrea.popularmoviespart2.features.details.DetailsContract;
import com.andrea.popularmoviespart2.features.details.logic.DetailsPresenter;
import com.andrea.popularmoviespart2.util.GlideUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View {

    ActivityDetailsBinding binding;

    @OnClick(R.id.details_play_trailer_button)
    public void onWatchTrailerSelected() {
        presenter.watchTrailerSelected();
    }

    @Inject
    DetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        ButterKnife.bind(this);

        DaggerDetailsComponent.builder()
                .appComponent(MovieApplication.getDagger())
                .build()
                .inject(this);

        presenter.connectView(this, getIntent());

        binding.detailsMovieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.detailsMovieReviewsRecyclerView.setHasFixedSize(true);
        binding.detailsMovieReviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.disconnectView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // region View methods
    @Override
    public void renderScreenTitle(@NonNull String title) {
        setTitle(title);
    }

    @Override
    public void renderMovieTitle(@NonNull String title) {
        binding.detailsMovieTitleTextView.setText(title);
    }

    @Override
    public void renderReleaseDate(@NonNull String releaseDate) {
        binding.detailsReleaseDateTextView.setText(releaseDate);
    }

    @Override
    public void renderVoteAverage(@NonNull String voteAverage, float rating) {
        binding.detailsMovieRatingBar.setRating(rating);
        binding.detailsMovieRatingTextView.setText(voteAverage);
    }

    @Override
    public void renderPlotSynopsis(@NonNull String plotSynopsis) {
        binding.detailsPlotSynopsisTextView.setText(plotSynopsis);
    }

    @Override
    public void renderPosterImage(@NonNull String posterPath) {
        GlideUtil.displayImage(posterPath, binding.detailsPosterImageView);
    }

    @Override
    public void renderBackdropPhoto(@NonNull String backdropPhotoPath) {GlideUtil.displayImage(backdropPhotoPath, binding.detailsBackdropImageView); }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void getMovieTrailerIntent(@NonNull Intent appIntent, @NonNull Intent webIntent) {
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void showError(@NonNull String errorTitle, @NonNull String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(errorTitle)
                .setMessage(errorMessage)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // do nothing
                });
        builder.create();
        builder.show();
    }

    @Override
    public void showProgressBar() {
        binding.loadingProgressBar.setVisibility(VISIBLE);
        binding.divider.setVisibility(GONE);
        binding.detailsUserReviewLabel.setVisibility(GONE);
        binding.detailsMovieReviewsRecyclerView.setVisibility(GONE);
    }

    @Override
    public void hideProgressBar() {
        binding.loadingProgressBar.setVisibility(GONE);
        binding.divider.setVisibility(VISIBLE);
        binding.detailsUserReviewLabel.setVisibility(VISIBLE);
        binding.detailsMovieReviewsRecyclerView.setVisibility(VISIBLE);
    }

    @Override
    public void showMovieReviews(@NonNull List<MovieReview> movieReviews) {
        DetailsAdapter adapter = new DetailsAdapter(movieReviews);
        binding.detailsMovieReviewsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void renderReviewLabel(@NonNull String label) {
        binding.detailsUserReviewLabel.setText(label);
    }
    // endregion
}
