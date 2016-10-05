package com.github.shareme.greenandroid.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface Selectable<T> {
    T withSelectable(boolean selectable);

    boolean isSelectable();
}
