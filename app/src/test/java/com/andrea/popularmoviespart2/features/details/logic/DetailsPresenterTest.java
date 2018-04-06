package com.andrea.popularmoviespart2.features.details.logic;

import android.content.Context;

import com.andrea.popularmoviespart2.BaseUnitTest;
import com.andrea.popularmoviespart2.features.common.ContentResolver;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.details.DetailsContract;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

public class DetailsPresenterTest extends BaseUnitTest {

    @Mock private Context context;
    @Mock private MovieRepository movieRepository;
    @Mock private ContentResolver contentResolver;
    @Mock private DetailsContract.View view;

    @Test
    public void renderScreenTitle_bundleEmpty_finishActivity() {
        // Setup
        DetailsPresenter presenter = new DetailsPresenter(context, movieRepository, contentResolver);

        // Run
        presenter.connectView(view, null);

        // Verify
        verify(view).finishActivity();
    }
}
