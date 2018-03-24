package com.andrea.popularmoviespart2.application;

import android.app.Application;

import com.andrea.popularmoviespart2.dagger.component.AppComponent;
import com.andrea.popularmoviespart2.dagger.component.DaggerAppComponent;
import com.andrea.popularmoviespart2.dagger.module.AppModule;
import com.andrea.popularmoviespart2.dagger.module.NetModule;
import com.facebook.stetho.Stetho;

public class MovieApplication extends Application {

    private static MovieApplication application;

    private AppComponent appComponent;

    @Override public void onCreate() {
        super.onCreate();

        application = this;

        appComponent = createDaggerComponent();

        Stetho.initializeWithDefaults(this);
    }

    private AppComponent createDaggerComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://api.themoviedb.org/3/"))
                .build();
    }

    public static AppComponent getDagger() {
        return application.appComponent;
    }
}
