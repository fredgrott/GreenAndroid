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

import java.util.List;

/**
 * Created by mikepenz on 27.12.15.
 */
public interface IAdapter<Item extends IItem> {
    /**
     * defines the FastAdapter which manages all the core logic
     *
     * @return the FastAdapter specified for this IAdapter
     */
    FastAdapter<Item> getFastAdapter();

    /**
     * defines in which order this adapter should be hooked into the FastAdapter
     *
     * @return the order of this adapter
     */
    int getOrder();

    /**
     * defines the count of items of THIS adapter
     *
     * @return the count of items of THIS adapter
     */
    int getAdapterItemCount();

    /**
     * @return the list of defined items within THIS adapter
     */
    List<Item> getAdapterItems();

    /**
     * @param position the relative position
     * @return the item at the given relative position within this adapter
     */
    Item getAdapterItem(int position);

    /**
     * Searches for the given item and calculates it's relative position
     *
     * @param item the item which is searched for
     * @return the relative position
     */
    int getAdapterPosition(Item item);

    /**
     * Searches for the given item and calculates it's relative position
     *
     * @param identifier the identifier of an item which is searched for
     * @return the relative position
     */
    int getAdapterPosition(long identifier);


    /**
     * Returns the global position based on the relative position given
     *
     * @param position the relative position within this adapter
     * @return the global position used for all methods
     */
    int getGlobalPosition(int position);

    /**
     * @return the global item count
     */
    int getItemCount();

    /**
     * @param position the global position
     * @return the global item based on the global position
     */
    Item getItem(int position);
}
