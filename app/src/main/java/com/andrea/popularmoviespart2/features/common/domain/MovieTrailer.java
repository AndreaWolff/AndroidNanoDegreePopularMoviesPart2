package com.andrea.popularmoviespart2.features.common.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class MovieTrailer implements Parcelable {

    private final String id;
    private final String key;
    private final String name;
    private final String site;
    private final String type;

    public MovieTrailer(@NonNull String id,
                        @NonNull String key,
                        @NonNull String name,
                        @NonNull String site,
                        @NonNull String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    @NonNull public String getId() {
        return id;
    }

    @NonNull public String getKey() {
        return key;
    }

    @NonNull public String getName() {
        return name;
    }

    @NonNull public String getSite() {
        return site;
    }

    @NonNull public String getType() {
        return type;
    }

    public Uri getYouTubeTrailerAppUrl() {
        return Uri.parse("vnd.youtube:" + key);
    }

    public Uri getYouTubeTrailerWebUrl() {
        return Uri.parse("http://www.youtube.com/watch?v=" + key);
    }

    protected MovieTrailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
