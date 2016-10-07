/*
 Copyright 2015 Satoru Fujiwara
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
package com.github.shareme.greenandroid.materialscrolling;

import android.view.View;
import android.view.ViewGroup;

import com.github.shareme.greenandroid.observablescrollview.ObservableRecyclerView;


final class Utils {

    private Utils() {

    }

    static ObservableRecyclerView findRecyclerView(final View view) {
        if (view instanceof ObservableRecyclerView) {
            return (ObservableRecyclerView) view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup group = (ViewGroup) view;
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            ObservableRecyclerView child = findRecyclerView(group.getChildAt(i));
            if (child != null) {
                return child;
            }
        }
        return null;
    }

}
