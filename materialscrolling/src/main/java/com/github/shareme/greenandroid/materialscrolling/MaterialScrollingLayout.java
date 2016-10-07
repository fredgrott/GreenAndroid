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


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.shareme.greenandroid.observablescrollview.ObservableRecyclerView;

@SuppressWarnings("unused")
public class MaterialScrollingLayout extends FrameLayout {

    private int flexibleHeight;
    private final BehaviorDispatcher behaviorDispatcher = new BehaviorDispatcher();
    private RecyclerViewHolder recyclerViewHolder;

    public MaterialScrollingLayout(final Context context) {
        this(context, null, 0);
    }

    public MaterialScrollingLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialScrollingLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int flexibleHeight = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ms_MaterialScrolling);
        flexibleHeight = a.getDimensionPixelSize(
                R.styleable.ms_MaterialScrolling_ms_flexible_height, flexibleHeight);
        a.recycle();
        this.flexibleHeight = flexibleHeight;
        behaviorDispatcher.setFlexibleHeight(flexibleHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        behaviorDispatcher.onAttachedToWindow(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        behaviorDispatcher.onDetachedFromWindow(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void addView(final View child, final int index, final ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        ObservableRecyclerView recyclerView = Utils.findRecyclerView(child);
        if (recyclerView == null) {
            return;
        }
        recyclerViewHolder = new RecyclerViewHolder(recyclerView, behaviorDispatcher);
        recyclerViewHolder.setFlexibleHeight(flexibleHeight);
        recyclerViewHolder.setIsDispatchScroll(true);
    }

    @Override
    public void removeView(final View view) {
        recyclerViewHolder.setIsDispatchScroll(false);
        recyclerViewHolder = null;
        super.removeView(view);
    }

    public void addBehavior(final View target, final Behavior behavior) {
        behaviorDispatcher.addBehavior(target, behavior);
    }

    public void setFlexibleHeight(final int flexibleHeight) {
        this.flexibleHeight = flexibleHeight;
        behaviorDispatcher.setFlexibleHeight(flexibleHeight);
        recyclerViewHolder.setFlexibleHeight(flexibleHeight);
    }
}
