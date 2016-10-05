package com.github.shareme.greenandroid.materialdrawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.github.shareme.greenandroid.fastadapter.IItem;
import com.github.shareme.greenandroid.materialdrawer.R;
import com.github.shareme.greenandroid.materialdrawer.holder.ColorHolder;


/**
 * Created by mikepenz on 03.02.15.
 */
public class SecondarySwitchDrawerItem extends AbstractSwitchableDrawerItem<SecondarySwitchDrawerItem> {

    @Override
    public int getType() {
        return R.id.material_drawer_item_secondary_switch;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_secondary_switch;
    }

    @Override
    public void unbindView(ViewHolder holder) {

    }

    /**
     * helper method to decide for the correct color
     * OVERWRITE to get the correct secondary color
     *
     * @param ctx
     * @return
     */
    @Override
    protected int getColor(Context ctx) {
        int color;
        if (this.isEnabled()) {
            color = ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text);
        } else {
            color = ColorHolder.color(getDisabledTextColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
        }
        return color;
    }

    @Override
    public IItem getParent() {
        return null;
    }

    @Override
    public Object withParent(IItem parent) {
        return null;
    }
}
