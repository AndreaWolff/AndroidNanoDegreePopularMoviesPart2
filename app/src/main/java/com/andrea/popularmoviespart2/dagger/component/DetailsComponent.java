package com.andrea.popularmoviespart2.dagger.component;

import com.andrea.popularmoviespart2.dagger.module.DetailsModule;
import com.andrea.popularmoviespart2.dagger.scope.PerActivity;
import com.andrea.popularmoviespart2.features.details.ui.DetailsActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = DetailsModule.class)
public interface DetailsComponent {
    void inject(DetailsActivity activity);
}
