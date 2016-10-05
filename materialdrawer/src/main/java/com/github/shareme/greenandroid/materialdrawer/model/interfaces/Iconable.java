package com.github.shareme.greenandroid.materialdrawer.model.interfaces;

import android.graphics.drawable.Drawable;

import com.github.shareme.greenandroid.materialdrawer.holder.ImageHolder;
import com.mikepenz.iconics.typeface.IIcon;


/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface Iconable<T> {
    T withIcon(Drawable icon);

    T withIcon(IIcon iicon);

    T withIcon(ImageHolder icon);

    ImageHolder getIcon();
}
