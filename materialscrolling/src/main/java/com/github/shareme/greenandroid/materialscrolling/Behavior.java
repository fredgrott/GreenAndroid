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

import com.github.shareme.greenandroid.observablescrollview.ScrollState;


public abstract class Behavior {

    private int flexibleHeight;

    void setFlexibleHeight(final int flexibleHeight) {
        this.flexibleHeight = flexibleHeight;
    }

    protected void onAttached(final View target) {

    }

    protected int getFlexibleHeight() {
        return flexibleHeight;
    }

    protected void onScrolled(final View target, final int scrollY, final int dy) {

    }

    protected void onDownMotionEvent() {

    }

    protected void onUpOrCancelMotionEvent(final ScrollState scrollState) {

    }

    protected void onGlobalLayout(final View target) {

    }
}
