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
public interface IExpandable<T, Item extends IItem & ISubItem> {
    /**
     * @return true if expanded (opened)
     */
    boolean isExpanded();

    /**
     * use this method to set if the Expandable item is currently expanded
     *
     * @param collapsed true if expanded (opened)
     * @return this
     */
    T withIsExpanded(boolean collapsed);

    /**
     * use this method to set the subItems of this item
     *
     * @param subItems the subItems for this Expandable Item
     * @return this
     */
    T withSubItems(List<Item> subItems);

    /**
     * @return the list of subItems
     */
    List<Item> getSubItems();

    /**
     * overwrite this method and return true if the item should auto expand on click, false if you want to disable this
     *
     * @return true if this item should auto expand in the adapter
     */
    boolean isAutoExpanding();
}
