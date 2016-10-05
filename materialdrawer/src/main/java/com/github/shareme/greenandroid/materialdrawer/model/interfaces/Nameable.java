package com.github.shareme.greenandroid.materialdrawer.model.interfaces;


import com.github.shareme.greenandroid.materialdrawer.holder.StringHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface Nameable<T> {
    T withName(String name);

    T withName(int nameRes);

    T withName(StringHolder name);

    StringHolder getName();
}
