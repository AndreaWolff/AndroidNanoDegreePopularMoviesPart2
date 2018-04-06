package com.andrea.popularmoviespart2.util;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtil {

    public static void displayImage(@NonNull String photo, @NonNull ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(photo)
                .into(imageView);
    }
}
