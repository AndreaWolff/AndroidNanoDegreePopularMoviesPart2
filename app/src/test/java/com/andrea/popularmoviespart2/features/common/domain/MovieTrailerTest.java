package com.andrea.popularmoviespart2.features.common.domain;

import com.andrea.popularmoviespart2.BaseUnitTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MovieTrailerTest extends BaseUnitTest {

    @Test
    public void movieTrailer_getters() {
        MovieTrailer movieTrailer = new MovieTrailer("12345",
                                                     "123456",
                                                     "The Hunt for the Red October",
                                                     "YouTube",
                                                     "media");

        assertThat(movieTrailer.getId(), is("12345"));
        assertThat(movieTrailer.getKey(), is("123456"));
        assertThat(movieTrailer.getName(), is("The Hunt for the Red October"));
        assertThat(movieTrailer.getSite(), is("YouTube"));
        assertThat(movieTrailer.getType(), is("media"));
    }
}