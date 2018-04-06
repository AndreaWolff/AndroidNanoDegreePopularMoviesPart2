package com.andrea.popularmoviespart2.dagger.module;

import android.support.annotation.NonNull;

import com.andrea.popularmoviespart2.features.common.ContentResolver;
import com.andrea.popularmoviespart2.features.common.ContentResolverDefault;
import com.andrea.popularmoviespart2.features.common.repository.MovieDao;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepository;
import com.andrea.popularmoviespart2.features.common.repository.MovieRepositoryDefault;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Module
public class NetModule {

    private String BASE_URL;

    public NetModule(@NonNull String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    @Singleton
    @Provides
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BODY))
                .build();
    }

    @Singleton @Provides
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    @Singleton @Provides
    MovieDao movieDao(Retrofit retrofit) {
        return retrofit.create(MovieDao.class);
    }

    @Singleton @Provides
    MovieRepository movieRepository(MovieRepositoryDefault impl) {
        return impl;
    }

    @Singleton @Provides
    ContentResolver contentResolver(ContentResolverDefault impl) { return  impl; }

}
