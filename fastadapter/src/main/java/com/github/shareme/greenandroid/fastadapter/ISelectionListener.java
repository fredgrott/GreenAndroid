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

/**
 * Created by flisar on 21.09.2016.
 */

public interface ISelectionListener<Item extends IItem> {
    /**
     * is called, whenever the provided item is selected or deselected
     *
     * @param item the item who's selection state changed
     *             param selected the new selection state of the item
     */
    void onSelectionChanged(Item item, boolean selected);
}
