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

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by mikepenz on 30.12.15.
 */
public interface IExtendedDraggable<T, VH extends RecyclerView.ViewHolder, Item extends IItem> extends IDraggable<T, Item> {

    /**
     * use this method to set the ItemTouchHelper reference in the item
     * this is necessary, so that the item can manually start the dragging
     * i.e when a drag icon within the item is touched
     *
     * @param itemTouchHelper the ItemTouchHelper
     * @return this
     */
    T withTouchHelper(ItemTouchHelper itemTouchHelper);

    /**
     * this returns the ItemTouchHelper
     *
     * @return the ItemTouchHelper if item has one or null
     */
    ItemTouchHelper getTouchHelper();

    /**
     * this method returns the drag view inside the item
     * use this with (@withTouchHelper) to start dragging when this view is touched
     *
     * @param viewHolder the ViewHolder
     * @return the view that should start the dragging or null
     */
    View getDragView(VH viewHolder);
}
