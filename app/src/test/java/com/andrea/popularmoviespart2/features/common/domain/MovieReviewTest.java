package com.andrea.popularmoviespart2.features.common.domain;

import com.andrea.popularmoviespart2.BaseUnitTest;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MovieReviewTest extends BaseUnitTest {

    @Test
    public void movieReview_getters() {
        MovieReview movieReview = new MovieReview("Author 1",
                                                  "This is a great movie!",
                                                  "12345",
                                                  "");

        assertThat(movieReview.getAuthor(), is("Author 1"));
        assertThat(movieReview.getContent(), is("This is a great movie!"));
        assertThat(movieReview.getId(), is("12345"));
        assertThat(movieReview.getUrl(), is(""));
    }
}