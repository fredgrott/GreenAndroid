package com.github.shareme.greenandroid.materialdrawer.model.interfaces;


import com.github.shareme.greenandroid.materialize.holder.StringHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface Badgeable<T> {
    T withBadge(String badge);

    T withBadge(int badgeRes);

    T withBadge(StringHolder badgeRes);

    StringHolder getBadge();
}
