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


import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewTreeObserver;

import com.github.shareme.greenandroid.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BehaviorDispatcher {

    private final List<View> targets = new ArrayList<>();
    private final ArrayMap<View, Behavior> behaviors = new ArrayMap<>();
    private int flexibleHeight;

    public BehaviorDispatcher() {
    }

    public void setFlexibleHeight(final int flexibleHeight) {
        this.flexibleHeight = flexibleHeight;
        for (Behavior behavior : behaviors.values()) {
            behavior.setFlexibleHeight(flexibleHeight);
        }
    }

    private final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            for (View target : targets) {
                behaviors.get(target).onGlobalLayout(target);
            }
        }
    };

    public void onAttachedToWindow(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    public void onDetachedFromWindow(final View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        }
    }

    public void addBehavior(final View target, final Behavior behavior) {
        targets.add(target);
        behavior.setFlexibleHeight(flexibleHeight);
        behaviors.put(target, behavior);
        behavior.onAttached(target);
    }

    void onScrolled(int scrollY, int dy) {
        for (View target : targets) {
            behaviors.get(target).onScrolled(target, scrollY, dy);
        }
    }

    void onDownMotionEvent() {
        for (View target : targets) {
            behaviors.get(target).onDownMotionEvent();
        }
    }

    void onUpOrCancelMotionEvent(ScrollState scrollState) {
        for (View target : targets) {
            behaviors.get(target).onUpOrCancelMotionEvent(scrollState);
        }
    }

}
