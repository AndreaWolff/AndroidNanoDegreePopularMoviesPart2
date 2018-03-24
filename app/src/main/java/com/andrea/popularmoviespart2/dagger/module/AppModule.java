package com.andrea.popularmoviespart2.dagger.module;

import android.content.Context;
import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.application.MovieApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final MovieApplication application;

    public AppModule(@NonNull MovieApplication application) {
        this.application = application;
    }

    @Singleton @Provides Context context() {
        return application.getApplicationContext();
    }

}
