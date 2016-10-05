package com.github.shareme.greenandroid.materialdrawer.model.interfaces;

import android.view.View;

/**
 * Created by mikepenz on 21.08.15.
 */
@SuppressWarnings("unused")
public interface OnPostBindViewListener {
    /**
     * allows you to hook in the BindView method and modify the view after binding
     *
     * @param drawerItem the drawerItem used for this view
     * @param view       the view which will be set
     */
    void onBindView(IDrawerItem drawerItem, View view);
}
