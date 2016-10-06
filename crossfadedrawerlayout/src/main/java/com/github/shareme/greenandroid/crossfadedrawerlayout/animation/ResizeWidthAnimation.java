/*
  Copyright 2016 Mike Penz
  Modifications Copyright(C) Fred Grott(GrottWorkShop)

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
package com.github.shareme.greenandroid.crossfadedrawerlayout.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.github.shareme.greenandroid.crossfadedrawerlayout.ApplyTransformationListener;


/**
 * animate the resizing if TOUCH_UP
 */
@SuppressWarnings("unused")
public class ResizeWidthAnimation extends Animation {
    private int mWidth;
    private int mStartWidth;
    private View mView;

    private ApplyTransformationListener mApplyTransformationListener;

    public ResizeWidthAnimation(View view, int width, ApplyTransformationListener applyTransformationListener) {
        mView = view;
        mWidth = width;
        mApplyTransformationListener = applyTransformationListener;
        mStartWidth = view.getWidth();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
        mView.getLayoutParams().width = newWidth;
        //change opacity
        mApplyTransformationListener.applyTransformation(newWidth);
        mView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}