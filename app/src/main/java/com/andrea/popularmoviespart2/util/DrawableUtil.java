package com.andrea.popularmoviespart2.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableUtil {

    public static Drawable getTintedDrawable(@NonNull Context context, @DrawableRes int drawableRes, int colourRes) {
        Drawable wrapDrawable = DrawableCompat.wrap(context.getResources().getDrawable(drawableRes));
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(colourRes));
        return wrapDrawable;
    }
}
