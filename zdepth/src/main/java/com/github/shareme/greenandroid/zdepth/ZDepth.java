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
package com.github.shareme.greenandroid.zdepth;

import android.content.Context;

import com.github.shareme.greenandroid.zdepth.utils.DisplayUtils;


public enum ZDepth {

    Depth0( // TODO
            0,
            0,
            0,
            0,
            0,
            0
    ),

    Depth1(
            30, // alpha to black
            61, // alpha to black
            1.0f, // dp
            1.0f, // dp
            1.5f, // dp
            1.0f  // dp
    ),
    Depth2(
            40,
            58,
            3.0f,
            3.0f,
            3.0f,
            3.0f
    ),
    Depth3(
            48,
            58,
            10.0f,
            6.0f,
            10.0f,
            3.0f
    ),
    Depth4(
            64,
            56,
            14.0f,
            10.0f,
            14.0f,
            5.0f
    ),
    Depth5(
            76,
            56,
            19.0f,
            15.0f,
            19.0f,
            6.0f
    );

    public final int mAlphaTopShadow; // alpha to black
    public final int mAlphaBottomShadow; // alpha to black

    public final float mOffsetYTopShadow; // dp
    public final float mOffsetYBottomShadow; // dp

    public final float mBlurTopShadow; // dp
    public final float mBlurBottomShadow; // dp

    ZDepth(int alphaTopShadow, int alphaBottomShadow, float offsetYTopShadow, float offsetYBottomShadow, float blurTopShadow, float blurBottomShadow) {
        mAlphaTopShadow = alphaTopShadow;
        mAlphaBottomShadow = alphaBottomShadow;
        mOffsetYTopShadow = offsetYTopShadow;
        mOffsetYBottomShadow = offsetYBottomShadow;
        mBlurTopShadow = blurTopShadow;
        mBlurBottomShadow = blurBottomShadow;
    }

    public int getAlphaTopShadow() {
        return mAlphaTopShadow;
    }

    public int getAlphaBottomShadow() {
        return mAlphaBottomShadow;
    }

    public float getOffsetYTopShadowPx(Context context) {
        return DisplayUtils.convertDpToPx(context, mOffsetYTopShadow);
    }

    public float getOffsetYBottomShadowPx(Context context) {
        return DisplayUtils.convertDpToPx(context, mOffsetYBottomShadow);
    }

    public float getBlurTopShadowPx(Context context) {
        return DisplayUtils.convertDpToPx(context, mBlurTopShadow);
    }

    public float getBlurBottomShadowPx(Context context) {
        return DisplayUtils.convertDpToPx(context, mBlurBottomShadow);
    }
}
