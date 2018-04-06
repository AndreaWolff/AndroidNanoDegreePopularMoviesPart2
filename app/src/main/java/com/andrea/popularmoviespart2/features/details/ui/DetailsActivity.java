package com.andrea.popularmoviespart2.features.details.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.Menu;
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

    private ActivityDetailsBinding binding;

    @OnClick(R.id.details_play_trailer_button)
    public void onWatchTrailerSelected() {
        presenter.watchTrailerSelected();
    }

    @OnClick(R.id.detailsFavoriteButton)
    public void onFavoriteButtonSelected() {
        presenter.favoriteSelected();
    }

    @Inject DetailsPresenter presenter;

    private boolean shareVisibility;

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

        binding.detailsUserReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.detailsUserReviewRecyclerView.setHasFixedSize(true);
        binding.detailsUserReviewRecyclerView.setNestedScrollingEnabled(false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(shareVisibility);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                presenter.shareYouTubeTrailer();
                return true;
            default:
                break;
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
    public void renderBackdropPhoto(@NonNull String backdropPhotoPath) {
        GlideUtil.displayImage(backdropPhotoPath, binding.detailsBackdropImageView);
    }

    @Override
    public void finishActivity() {
        finish();
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
        binding.detailsUserReviewLoadingProgressBar.setVisibility(VISIBLE);
        binding.detailsUserReviewDivider.setVisibility(GONE);
        binding.detailsUserReviewLabel.setVisibility(GONE);
        binding.detailsUserReviewRecyclerView.setVisibility(GONE);
    }

    @Override
    public void hideProgressBar() {
        binding.detailsUserReviewLoadingProgressBar.setVisibility(GONE);
        binding.detailsUserReviewDivider.setVisibility(VISIBLE);
        binding.detailsUserReviewLabel.setVisibility(VISIBLE);
        binding.detailsUserReviewRecyclerView.setVisibility(VISIBLE);
    }

    @Override
    public void showMovieReviews(@NonNull List<MovieReview> movieReviews) {
        DetailsAdapter adapter = new DetailsAdapter(movieReviews);
        binding.detailsUserReviewRecyclerView.setAdapter(adapter);
    }

    @Override
    public void renderReviewLabel(@NonNull String label) {
        binding.detailsUserReviewLabel.setText(Html.fromHtml(label));
    }

    @Override
    public void setFavoriteButton(@NonNull Drawable drawable) {
        binding.detailsFavoriteButton.setImageDrawable(drawable);
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
    public void shareYouTubeTrailer(@NonNull String type, @NonNull String youTubeTrailer) {
        Intent shareIntent = ShareCompat.IntentBuilder
                .from(this)
                .setType(type)
                .setText(youTubeTrailer)
                .getIntent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(shareIntent);
    }

    @Override
    public void showMovieTrailerButtons() {
        binding.detailsMovieTrailerContent.setVisibility(VISIBLE);
        shareVisibility = true;
        invalidateOptionsMenu();
    }

    @Override
    public void hideMovieTrailerButtons() {
        binding.detailsMovieTrailerContent.setVisibility(GONE);
        shareVisibility = false;
        invalidateOptionsMenu();
    }

    @Override
    public void showContentProgressBar() {
        binding.detailsLoadingProgressBar.setVisibility(VISIBLE);
        binding.contentConstraintLayout.setVisibility(GONE);
    }

    @Override
    public void hideContentProgressBar() {
        binding.detailsLoadingProgressBar.setVisibility(GONE);
        binding.contentConstraintLayout.setVisibility(VISIBLE);
    }
    // endregion
}
