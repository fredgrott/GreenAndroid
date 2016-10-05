/*
 * Copyright 2014 Google Inc.
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shareme.greenandroid.materialize.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.shareme.greenandroid.materialize.R;


/**
 * Created by mikepenz on 25.01.16.
 */
@SuppressWarnings("unused")
public class ScrimInsetsRelativeLayout extends RelativeLayout implements IScrimInsetsLayout {

    private Drawable mInsetForeground;
    private Rect mInsets;
    private Rect mTempRect = new Rect();

    private OnInsetsCallback mOnInsetsCallback;

    private boolean mTintStatusBar = true;
    private boolean mTintNavigationBar = true;
    private boolean mSystemUIVisible = true;

    public ScrimInsetsRelativeLayout(Context context) {
        this(context, null);
    }

    public ScrimInsetsRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrimInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ScrimInsetsRelativeLayout, defStyleAttr,
                R.style.Widget_Materialize_ScrimInsetsRelativeLayout);
        mInsetForeground = a.getDrawable(R.styleable.ScrimInsetsRelativeLayout_sirl_insetForeground);
        a.recycle();
        setWillNotDraw(true); // No need to draw until the insets are adjusted

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v,
                                                                  WindowInsetsCompat insets) {
                        if (null == mInsets) {
                            mInsets = new Rect();
                        }
                        mInsets.set(insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(),
                                insets.getSystemWindowInsetRight(),
                                insets.getSystemWindowInsetBottom());
                        setWillNotDraw(mInsetForeground == null);
                        ViewCompat.postInvalidateOnAnimation(ScrimInsetsRelativeLayout.this);
                        if (mOnInsetsCallback != null) {
                            mOnInsetsCallback.onInsetsChanged(insets);
                        }
                        return insets.consumeSystemWindowInsets();
                    }
                });
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        int width = getWidth();
        int height = getHeight();
        if (mInsets != null && mInsetForeground != null) {
            int sc = canvas.save();
            canvas.translate(getScrollX(), getScrollY());

            if (!mSystemUIVisible) {
                mInsets.top = 0;
                mInsets.right = 0;
                mInsets.bottom = 0;
                mInsets.left = 0;
            }

            // Top
            if (mTintStatusBar) {
                mTempRect.set(0, 0, width, mInsets.top);
                mInsetForeground.setBounds(mTempRect);
                mInsetForeground.draw(canvas);
            }

            // Bottom
            if (mTintNavigationBar) {
                mTempRect.set(0, height - mInsets.bottom, width, height);
                mInsetForeground.setBounds(mTempRect);
                mInsetForeground.draw(canvas);
            }
            // Left
            mTempRect.set(0, mInsets.top, mInsets.left, height - mInsets.bottom);
            mInsetForeground.setBounds(mTempRect);
            mInsetForeground.draw(canvas);

            // Right
            mTempRect.set(width - mInsets.right, mInsets.top, width, height - mInsets.bottom);
            mInsetForeground.setBounds(mTempRect);
            mInsetForeground.draw(canvas);

            canvas.restoreToCount(sc);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mInsetForeground != null) {
            mInsetForeground.setCallback(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mInsetForeground != null) {
            mInsetForeground.setCallback(null);
        }
    }

    @Override
    public ViewGroup getView() {
        return this;
    }

    @Override
    public Drawable getInsetForeground() {
        return mInsetForeground;
    }

    @Override
    public void setInsetForeground(Drawable mInsetForeground) {
        this.mInsetForeground = mInsetForeground;
    }

    @Override
    public void setInsetForeground(int mInsetForegroundColor) {
        this.mInsetForeground = new ColorDrawable(mInsetForegroundColor);
    }

    @Override
    public boolean isTintStatusBar() {
        return mTintStatusBar;
    }

    @Override
    public void setTintStatusBar(boolean mTintStatusBar) {
        this.mTintStatusBar = mTintStatusBar;
    }

    @Override
    public boolean isTintNavigationBar() {
        return mTintNavigationBar;
    }

    @Override
    public void setTintNavigationBar(boolean mTintNavigationBar) {
        this.mTintNavigationBar = mTintNavigationBar;
    }

    public boolean isSystemUIVisible() {
        return mSystemUIVisible;
    }

    public void setSystemUIVisible(boolean systemUIVisible) {
        this.mSystemUIVisible = systemUIVisible;
    }

    /**
     * Allows the calling container to specify a callback for custom processing when insets change (i.e. when
     * {@link #fitSystemWindows(Rect)} is called. This is useful for setting padding on UI elements based on
     * UI chrome insets (e.g. a Google Map or a ListView). When using with ListView or GridView, remember to set
     * clipToPadding to false.
     */
    @Override
    public void setOnInsetsCallback(OnInsetsCallback onInsetsCallback) {
        mOnInsetsCallback = onInsetsCallback;
    }

    @Override
    public OnInsetsCallback getOnInsetsCallback() {
        return mOnInsetsCallback;
    }
}
