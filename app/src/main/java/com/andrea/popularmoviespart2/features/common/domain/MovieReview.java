package com.andrea.popularmoviespart2.features.common.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class MovieReview implements Parcelable {

    private final String author;
    private final String content;
    private final String id;
    private final String url;

    public MovieReview(@NonNull String author,
                       @NonNull String content,
                       @NonNull String id,
                       @NonNull String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    @NonNull public String getAuthor() {
        return author;
    }

    @NonNull public String getContent() {
        return content;
    }

    @NonNull public String getId() {
        return id;
    }

    @NonNull public String getUrl() {
        return url;
    }

    protected MovieReview(Parcel in) {
        author = in.readString();
        content = in.readString();
        id = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}