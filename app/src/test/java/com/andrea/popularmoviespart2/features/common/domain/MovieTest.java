package com.andrea.popularmoviespart2.features.common.domain;

import com.andrea.popularmoviespart2.BaseUnitTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MovieTest extends BaseUnitTest {

    @Test
    public void movie_getters_isNotFavorite() {
        // Run
        Movie movie = createMovie(false);

        // Verify
        assertThat(movie.getId(), is("343611"));
        assertThat(movie.getTitle(), is("Jack Reacher: Never Go Back"));
        assertThat(movie.getReleaseDate(), is("2016-10-19"));
        assertThat(movie.getVoteAverage(), is(4.19f));
        assertThat(movie.getPlotSynopsis(), is("Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever."));
        assertThat(movie.getPosterPath(), is("http://image.tmdb.org/t/p/w185/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg"));
        assertThat(movie.getBackdropPhotoPath(), is("http://image.tmdb.org/t/p/w185/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg"));
    }

    @Test
    public void movie_getters_isFavorite() {
        // Run
        Movie movie = createMovie(true);

        // Verify
        assertThat(movie.getId(), is("343611"));
        assertThat(movie.getTitle(), is("Jack Reacher: Never Go Back"));
        assertThat(movie.getReleaseDate(), is("2016-10-19"));
        assertThat(movie.getVoteAverage(), is(4.19f));
        assertThat(movie.getPlotSynopsis(), is("Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever."));
        assertThat(movie.getPosterPath(), is("/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg"));
        assertThat(movie.getBackdropPhotoPath(), is("/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg"));
    }

    // region Test Helper
    private Movie createMovie(boolean isFavorite) {
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