package com.andrea.popularmoviespart2;


import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.MockitoAnnotations;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class BaseUnitTest {

    @BeforeClass
    public static void createMainThreadScheduler() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
