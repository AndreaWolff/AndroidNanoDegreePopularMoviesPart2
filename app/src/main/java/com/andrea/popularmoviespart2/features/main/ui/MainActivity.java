package com.andrea.popularmoviespart2.features.main.ui;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import static com.andrea.popularmoviespart2.features.common.ActivityConstants.MOVIE_LOADER_ID;

public class MainActivity extends AppCompatActivity implements MainContract.View, MainAdapter.ListItemClickListener,
                                                               FavoriteMoviesAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    ActivityMainBinding binding;

    @Inject MainPresenter presenter;

    private MainAdapter adapter;
    private FavoriteMoviesAdapter favoriteMoviesAdapter;
    private boolean favorite;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        DaggerMainComponent.builder()
                .appComponent(getDagger())
                .build()
                .inject(this);

        presenter.connectView(this, savedInstanceState);

        // Taken from https://guides.codepath.com/android/implementing-pull-to-refresh-guide#step-2-setup-swiperefreshlayout
        binding.swipeToRefreshContainer.setOnRefreshListener(() -> presenter.swipeToRefresh());
        binding.swipeToRefreshContainer.setColorSchemeResources(R.color.colorAccent);

        binding.mainMoviePostersRecyclerView.setLayoutManager(new GridLayoutManager(this, this.getResources().getInteger(R.integer.grid_span_count)));
        binding.mainMoviePostersRecyclerView.setHasFixedSize(true);

        binding.mainFavoriteMoviePostersRecyclerView.setLayoutManager(new GridLayoutManager(this, getApplicationContext().getResources().getInteger(R.integer.grid_span_count)));
        binding.mainFavoriteMoviePostersRecyclerView.setHasFixedSize(true);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSavedInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
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
                favorite = false;
                presenter.loadPopularMovies();
                return true;
            case R.id.menu_top_rated_movies:
                favorite = false;
                presenter.loadTopRatedMovies();
                return true;
            case R.id.menu_favorite_movies:
                presenter.loadFavoriteMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // endregion

    // region Adapter
    @Override public void onListItemClick(Movie movie) {
        presenter.onMoviePosterSelected(movie);
    }
    // endregion

    // region Favorite Movies
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return presenter.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        presenter.onLoadFinished(data, favorite);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        presenter.onLoaderReset();
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
        adapter = new MainAdapter(this, movieList);
        binding.mainMoviePostersRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showFavoriteMoviesList() {
        binding.mainFavoriteMoviePostersRecyclerView.setVisibility(VISIBLE);
        binding.mainMoviePostersRecyclerView.setVisibility(GONE);
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
        binding.mainFavoriteMoviePostersRecyclerView.setVisibility(GONE);
        binding.loadingProgressBar.setVisibility(VISIBLE);
    }

    @Override public void hideProgressBar() {
        binding.mainMoviePostersRecyclerView.setVisibility(VISIBLE);
        binding.loadingProgressBar.setVisibility(GONE);
    }

    @Override public void hideProgressBarOnMovieListError() {
        binding.mainMoviePostersRecyclerView.setVisibility(GONE);
        binding.mainFavoriteMoviePostersRecyclerView.setVisibility(GONE);
        binding.loadingProgressBar.setVisibility(GONE);
    }

    @Override public void navigateToMovieDetails(@NonNull Intent intent) {
        startActivity(intent);
    }

    @Override
    public void configureFavoriteMovieLoader(int loaderId, boolean isFavorite) {
        favorite = isFavorite;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void configureFavoriteMoviesAdapter() {
        favoriteMoviesAdapter = new FavoriteMoviesAdapter(this);
        binding.mainFavoriteMoviePostersRecyclerView.setAdapter(favoriteMoviesAdapter);
    }

    @Override
    public void swapCursor(@Nullable Cursor data) {
        favoriteMoviesAdapter.swapCursor(data);
    }

    @Override
    public void clearMovieCache() {
        adapter.clear();
        binding.swipeToRefreshContainer.setRefreshing(false);
    }
    // endregion
}
