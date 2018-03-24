package com.andrea.popularmoviespart2.features.main.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.dagger.component.DaggerMainComponent;
import com.andrea.popularmoviespart2.databinding.ActivityMainBinding;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.main.MainContract;
import com.andrea.popularmoviespart2.features.main.logic.MainPresenter;

import java.util.List;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.andrea.popularmoviespart2.application.MovieApplication.getDagger;

public class MainActivity extends AppCompatActivity implements MainContract.View, MainAdapter.ListItemClickListener {

    ActivityMainBinding binding;

    @Inject
    MainPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        DaggerMainComponent.builder()
                .appComponent(getDagger())
                .build()
                .inject(this);

        presenter.connectView(this, savedInstanceState);

        binding.mainMoviePostersRecyclerView.setLayoutManager(new GridLayoutManager(this, getApplicationContext().getResources().getInteger(R.integer.grid_span_count)));
        binding.mainMoviePostersRecyclerView.setHasFixedSize(true);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSavedInstanceState(outState);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.disconnectView();
    }

    // region Settings menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_popular_movies:
                presenter.loadPopularMovies();
                return true;
            case R.id.menu_top_rated_movies:
                presenter.loadTopRatedMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // endregion

    // region Main Adapter
    @Override public void onListItemClick(Movie movie) {
        presenter.onMoviePosterSelected(movie);
    }
    // endregion

    // region View Methods
    @Override public void renderPopularMoviesTitle(@NonNull String title) {
        setTitle(title);
    }

    @Override public void renderTopRatedMoviesTitle(@NonNull String title) {
        setTitle(title);
    }

    @Override public void showMoviesList(@NonNull List<Movie> movieList) {
        MainAdapter adapter = new MainAdapter(this, movieList);
        binding.mainMoviePostersRecyclerView.setAdapter(adapter);
    }

    @Override public void showError(@NonNull String title, @NonNull String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(errorMessage)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // do nothing
                });
        builder.create();
        builder.show();
    }

    @Override public void showProgressBar() {
        binding.mainMoviePostersRecyclerView.setVisibility(GONE);
        binding.loadingProgressBar.setVisibility(VISIBLE);
    }

    @Override public void hideProgressBar() {
        binding.mainMoviePostersRecyclerView.setVisibility(VISIBLE);
        binding.loadingProgressBar.setVisibility(GONE);
    }

    @Override public void hideProgressBarOnMovieListError() {
        binding.mainMoviePostersRecyclerView.setVisibility(GONE);
        binding.loadingProgressBar.setVisibility(GONE);
    }

    @Override public void navigateToMovieDetails(@NonNull Intent intent) {
        startActivity(intent);
    }
    // endregion
}
