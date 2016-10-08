/*
 *  Copyright © 2016, Turing Technologies, an unincorporated organisation of Wynne Plaga
 *  Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.shareme.greenandroid.materialscrollbar;

public interface ICustomScroller {

    /**
     * @param index The index of the relevant element.
     * @return An integer in pixels representing the depth of the item within the recyclerView.
     * Usually just the sum of the height of all elements which appear above it in the recyclerView.
     */
    int getDepthForItem(int index);

    /**
     * @return An integer representing the index of the item which should be scrolled to when the
     * user clicks at the specified length down the bar. For example, if "progress" is 0.5F then you
     * should return the index of the item which is half-way down the recyclerView.
     */
    int getItemIndexForScroll(float progress);

    /**
     * @return The sum of the heights of all the views in the recyclerView.
     */
    int getTotalDepth();

}
