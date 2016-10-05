package com.github.shareme.greenandroid.materialdrawer.model.interfaces;

import android.graphics.Typeface;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface Typefaceable<T> {
    T withTypeface(Typeface typeface);

    Typeface getTypeface();
}
