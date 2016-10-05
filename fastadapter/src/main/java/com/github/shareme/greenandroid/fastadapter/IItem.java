/*
  Copyright 2016 Mike Penz
  Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */
package com.github.shareme.greenandroid.fastadapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
@SuppressWarnings("unuseed")
public interface IItem<T, VH extends RecyclerView.ViewHolder> extends IIdentifyable<T> {

    /**
     * return a Tag of the Item
     *
     * @return
     */
    Object getTag();

    /**
     * set the Tag of the Item
     *
     * @param tag
     * @return
     */
    T withTag(Object tag);

    /**
     * return if the item is enabled
     *
     * @return
     */
    boolean isEnabled();

    /**
     * set if the item is enabled
     *
     * @param enabled
     * @return
     */
    T withEnabled(boolean enabled);

    /**
     * return if the item is selected
     *
     * @return
     */
    boolean isSelected();

    /**
     * set if the item is selected
     *
     * @param selected
     * @return
     */
    T withSetSelected(boolean selected);

    /**
     * return if the item is selectable
     *
     * @return
     */
    boolean isSelectable();

    /**
     * set if the item is selectable
     *
     * @param selectable
     * @return
     */
    T withSelectable(boolean selectable);

    /**
     * returns the type of the Item. Can be a hardcoded INT, but preferred is a defined id
     *
     * @return
     */
    @IdRes
    int getType();

    /**
     * returns the layout for the given item
     *
     * @return
     */
    @LayoutRes
    int getLayoutRes();

    /**
     * generates a view by the defined LayoutRes
     *
     * @param ctx
     * @return
     */
    View generateView(Context ctx);

    /**
     * generates a view by the defined LayoutRes and pass the LayoutParams from the parent
     *
     * @param ctx
     * @param parent
     * @return
     */
    View generateView(Context ctx, ViewGroup parent);

    /**
     * Generates a ViewHolder from this Item with the given parent
     *
     * @param parent
     * @return
     */
    VH getViewHolder(ViewGroup parent);

    /**
     * Binds the data of this item to the given holder
     *
     * @param holder
     * @param payloads
     */
    void bindView(VH holder, List payloads);

    /**
     * View needs to release resources when it's recycled
     *
     * @param holder
     */
    void unbindView(VH holder);

    /**
     * If this item equals to the given identifier
     *
     * @param id
     * @return
     */
    boolean equals(int id);

}
