package com.andrea.popularmoviespart2.dagger.component;

import android.content.Context;

import com.andrea.popularmoviespart2.dagger.module.AppModule;
import com.andrea.popularmoviespart2.dagger.module.NetModule;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    MovieRepository getMovieRepository();

    Context getContext();
}
