package com.github.shareme.greenandroid.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface Tagable<T> {
    T withTag(Object tag);

    Object getTag();
}
