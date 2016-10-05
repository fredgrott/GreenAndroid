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
package com.github.shareme.greenandroid.fastadapter.utils;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * DiffCallback used to efficiently update the items inside the list with the new DiffUtils
 */
@SuppressWarnings("unused")
public interface DiffCallback<Item> {
    /**
     * Called by the DiffUtil to decide whether two object represent the same Item.
     * For example, if your items have unique ids, this method should check their id equality.
     *
     * @param oldItem the item in the old list
     * @param newItem the item in the new list
     * @return True if the two items represent the same object or false if they are different.
     */
    boolean areItemsTheSame(Item oldItem, Item newItem);

    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * For example, if you are using DiffUtil with a
     * {@link RecyclerView.Adapter RecyclerView.Adapter}, you should
     * return whether the items' visual representations are the same.
     * This method is called only if #areItemsTheSame(Item, Item) returns
     * {@code true} for these items.
     *
     * @param oldItem the item in the old list
     * @param newItem the item in the new list
     * @return True if the contents of the items are the same or false if they are different.
     */
    boolean areContentsTheSame(Item oldItem, Item newItem);

    /**
     * When {#areItemsTheSame(Item, Item)} returns {@code true} for two items and
     * {#areContentsTheSame(Item, Item)} returns false for them, DiffUtil
     * calls this method to get a payload about the change.
     * For example, if you are using DiffUtil with {@link RecyclerView}, you can return the
     * particular field that changed in the item and your
     * {@link RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * Default implementation returns {@code null}.
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return A payload object that represents the change between the two items.
     */
    @Nullable
    Object getChangePayload(Item oldItem, int oldItemPosition, Item newItem, int newItemPosition);
}