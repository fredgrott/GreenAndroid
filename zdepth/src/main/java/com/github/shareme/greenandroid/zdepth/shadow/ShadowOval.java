/*
 The MIT License (MIT)

Copyright (c) 2014 Shogo Mizumoto
Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package com.github.shareme.greenandroid.zdepth.shadow;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.github.shareme.greenandroid.zdepth.ZDepthParam;


public class ShadowOval implements Shadow {

    private ShapeDrawable mTopShadow;
    private ShapeDrawable mBottomShadow;

    private RectF mRectTopShadow;
    private RectF mRectBottomShadow;

    public ShadowOval() {
        mRectTopShadow = new RectF();
        mRectBottomShadow = new RectF();
        mTopShadow = new ShapeDrawable(new OvalShape());
        mBottomShadow = new ShapeDrawable(new OvalShape());
    }

    @Override
    public void setParameter(ZDepthParam param, int left, int top, int right, int bottom) {
        mRectTopShadow.left   = left;
        mRectTopShadow.top    = top    + param.mOffsetYTopShadowPx;
        mRectTopShadow.right  = right;
        mRectTopShadow.bottom = bottom + param.mOffsetYTopShadowPx;

        mRectBottomShadow.left   = left;
        mRectBottomShadow.top    = top    + param.mOffsetYBottomShadowPx;
        mRectBottomShadow.right  = right;
        mRectBottomShadow.bottom = bottom + param.mOffsetYBottomShadowPx;

        mTopShadow.getPaint().setColor(Color.argb(param.mAlphaTopShadow, 0, 0, 0));
        if (0 < param.mBlurTopShadowPx) {
            mTopShadow.getPaint().setMaskFilter(new BlurMaskFilter(param.mBlurTopShadowPx, BlurMaskFilter.Blur.NORMAL));
        } else {
            mTopShadow.getPaint().setMaskFilter(null);
        }

        mBottomShadow.getPaint().setColor(Color.argb(param.mAlphaBottomShadow, 0, 0, 0));
        if (0 < param.mBlurBottomShadowPx) {
            mBottomShadow.getPaint().setMaskFilter(new BlurMaskFilter(param.mBlurBottomShadowPx, BlurMaskFilter.Blur.NORMAL));
        } else {
            mBottomShadow.getPaint().setMaskFilter(null);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawOval(mRectBottomShadow, mBottomShadow.getPaint());
        canvas.drawOval(mRectTopShadow, mTopShadow.getPaint());
    }
}
