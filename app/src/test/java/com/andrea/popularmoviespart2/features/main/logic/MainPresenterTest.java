package com.andrea.popularmoviespart2.features.main.logic;

import android.content.Context;

import com.andrea.popularmoviespart2.BaseUnitTest;
import com.andrea.popularmoviespart2.R;
import com.andrea.popularmoviespart2.features.common.domain.Movie;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.main.MainContract;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MainPresenterTest extends BaseUnitTest {

    @Mock private MovieRepository movieRepository;
    @Mock private Context context;
    @Mock private MainContract.View view;

    @Captor private ArgumentCaptor<List<Movie>> movieListArgumentCaptor;

    private MainPresenter presenter;

    @Override public void setUp() throws Exception {
        super.setUp();

        presenter = new MainPresenter(movieRepository, context);
    }

    @Test
    public void renderPopularMoviesTitle() {
        // Setup
        when(context.getString(R.string.main_popular_movies_title)).thenReturn("Popular Movies");
        when(movieRepository.getPopularMovies()).thenReturn(Single.never());

        // Run
        presenter.connectView(view, null);

        // Verify
        verify(view, times(2)).renderPopularMoviesTitle("Popular Movies");
    }

    @Test
    public void connectView_loadPopularMovies_returnMovieList() {
        // Setup
        when(context.getString(R.string.main_popular_movies_title)).thenReturn("Popular Movies");
        when(movieRepository.getPopularMovies()).thenReturn(Single.just(getMovies(false)));

        // Run
        presenter.connectView(view, null);

        // Verify
        verify(view).showProgressBar();
        verify(view).hideProgressBar();
        verify(view, times(2)).renderPopularMoviesTitle("Popular Movies");
        verify(view).showMoviesList(movieListArgumentCaptor.capture());

        List<Movie> result = movieListArgumentCaptor.getValue();
        assertThat(result.get(0).getTitle(), is("Jack Reacher: Never Go Back"));
    }

    @Test
    public void loadPopularMovies_fromSettings_returnMovieList() {
        // Setup
        when(context.getString(R.string.main_popular_movies_title)).thenReturn("Popular Movies");
        when(movieRepository.getPopularMovies()).thenReturn(Single.just(getMovies(false)));
        presenter.connectView(view, null);
        reset(view);

        // Run
        presenter.loadPopularMovies();

        // Verify
        verify(view).showProgressBar();
        verify(view).hideProgressBar();
        verify(view).renderPopularMoviesTitle("Popular Movies");
        verify(view).showMoviesList(movieListArgumentCaptor.capture());

        List<Movie> result = movieListArgumentCaptor.getValue();
        assertThat(result.get(0).getTitle(), is("Jack Reacher: Never Go Back"));
    }

    @Test
    public void connectView_loadPopularMovies_defaultError_returnErrorDialog() {
        // Setup
        when(context.getString(R.string.error_title)).thenReturn("Error");
        when(context.getString(R.string.error_message)).thenReturn("Something went wrong, please try again later.");
        when(movieRepository.getPopularMovies()).thenReturn(Single.error(new Throwable("defaultError")));

        // Run
        presenter.connectView(view, null);

        // Verify
        verify(view).showProgressBar();
        verify(view).hideProgressBarOnMovieListError();
        verify(view).showError("Error", "Something went wrong, please try again later.");
    }

    @Test
    public void loadTopRatedMovies_returnMovieList() {
        // Setup
        when(context.getString(R.string.main_top_rated_movies_title)).thenReturn("Top-Rated Movies");
        when(movieRepository.getPopularMovies()).thenReturn(Single.never());
        when(movieRepository.getTopRatedMovies()).thenReturn(Single.just(getMovies(false)));
        presenter.connectView(view, null);
        reset(view);

        // Run
        presenter.loadTopRatedMovies();

        // Verify
        verify(view).showProgressBar();
        verify(view).hideProgressBar();
        verify(view).renderTopRatedMoviesTitle("Top-Rated Movies");
        verify(view).showMoviesList(movieListArgumentCaptor.capture());

        List<Movie> result = movieListArgumentCaptor.getValue();
        assertThat(result.get(0).getTitle(), is("Jack Reacher: Never Go Back"));
    }

    @Test
    public void loadTopRatedMovies_defaultError_returnErrorDialog() {
        // Setup
        when(context.getString(R.string.error_title)).thenReturn("Error");
        when(context.getString(R.string.error_message)).thenReturn("Something went wrong, please try again later.");
        when(movieRepository.getPopularMovies()).thenReturn(Single.never());
        when(movieRepository.getTopRatedMovies()).thenReturn(Single.error(new Throwable("defaultError")));
        presenter.connectView(view, null);
        reset(view);

        // Run
        presenter.loadTopRatedMovies();

        // Verify
        verify(view).showProgressBar();
        verify(view).hideProgressBarOnMovieListError();
        verify(view).showError("Error", "Something went wrong, please try again later.");
    }

    @Test
    public void disconnectView() {
        // Setup
        when(movieRepository.getPopularMovies()).thenReturn(Single.just(getMovies(false)));
        presenter.connectView(view, null);
        reset(view);

        // Run
        presenter.disconnectView();

        // Verify
        verifyZeroInteractions(view);
    }

    // region Test Helper
    private List<Movie> getMovies(boolean isFavorite) {
        List<Movie> movies = new ArrayList<>();
        movies.add(getMovie(isFavorite));
        return movies;
    }

    private Movie getMovie(boolean isFavorite) {
        return new Movie("343611",
                         "Jack Reacher: Never Go Back",
                         "2016-10-19",
                         4.19f,
                         "Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever.",
                         "/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg",
                         "/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg",
                         isFavorite);
    }
    // endregion
}