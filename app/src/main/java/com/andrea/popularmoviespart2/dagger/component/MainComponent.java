package com.andrea.popularmoviespart2.dagger.component;

import com.andrea.popularmoviespart2.dagger.module.MainModule;
import com.andrea.popularmoviespart2.dagger.scope.PerActivity;
import com.andrea.popularmoviespart2.features.main.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
