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
 * Created by mikepenz on 30.12.15.
 */
@SuppressWarnings("unused")
public interface IItemAdapter<Item extends IItem> extends IAdapter<Item> {

    /**
     * sets the subItems of the given collapsible
     *
     * @param collapsible the collapsible which gets the subItems set
     * @param subItems    the subItems for this collapsible item
     * @return the item type of the collapsible
     */
    <T extends IItem & IExpandable<T, S>, S extends IItem & ISubItem<Item, T>> T setSubItems(T collapsible, List<S> subItems);

    /**
     * set a new list of items and apply it to the existing list (clear - add) for this adapter
     *
     * @param items
     */
    IItemAdapter<Item> set(List<Item> items);

    /**
     * sets a complete new list of items onto this adapter, using the new list. Calls notifyDataSetChanged
     *
     * @param items
     */
    IItemAdapter<Item> setNewList(List<Item> items);

    /**
     * add an array of items to the end of the existing items
     *
     * @param items
     */
    @SuppressWarnings("unchecked")
    IItemAdapter<Item> add(Item... items);

    /**
     * add a list of items to the end of the existing items
     *
     * @param items
     */
    IItemAdapter<Item> add(List<Item> items);

    /**
     * add an array of items at the given position within the existing items
     *
     * @param position the global position
     * @param items
     */
    @SuppressWarnings("unchecked")
    IItemAdapter<Item> add(int position, Item... items);

    /**
     * add a list of items at the given position within the existing items
     *
     * @param position the global position
     * @param items
     */
    IItemAdapter<Item> add(int position, List<Item> items);

    /**
     * sets an item at the given position, overwriting the previous item
     *
     * @param position the global position
     * @param item
     */
    IItemAdapter<Item> set(int position, Item item);

    /**
     * removes an item at the given position within the existing icons
     *
     * @param position the global position
     */
    IItemAdapter<Item> remove(int position);

    /**
     * removes a range of items starting with the given position within the existing icons
     *
     * @param position  the global position
     * @param itemCount
     */
    IItemAdapter<Item> removeRange(int position, int itemCount);

    /**
     * removes all items of this adapter
     */
    IItemAdapter<Item> clear();

    /**
     * the interface used to filter the list inside the ItemFilter
     */
    interface Predicate<Item extends IItem> {
        /**
         * @param item       the item which is checked if it should get filtered
         * @param constraint the string constraint used to filter items away
         * @return false if it should stay. true if it should get filtered away
         */
        boolean filter(Item item, CharSequence constraint);
    }
}
