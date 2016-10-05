package com.github.shareme.greenandroid.materialdrawer.model.interfaces;


import com.github.shareme.greenandroid.materialdrawer.holder.BadgeStyle;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unused")
public interface ColorfulBadgeable<T> extends Badgeable<T> {
    T withBadgeStyle(BadgeStyle badgeStyle);

    BadgeStyle getBadgeStyle();

}
