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
package com.github.shareme.greenandroid.materialize.holder;

import android.content.Context;
import android.support.annotation.DimenRes;

import com.github.shareme.greenandroid.materialize.util.UIUtils;


/**
 * Created by mikepenz on 13.07.15.
 */
@SuppressWarnings("unused")
public class DimenHolder {
    private int mPixel = Integer.MIN_VALUE;
    private int mDp = Integer.MIN_VALUE;
    private int mResource = Integer.MIN_VALUE;

    public DimenHolder() {

    }

    public int getPixel() {
        return mPixel;
    }

    public void setPixel(int mPixel) {
        this.mPixel = mPixel;
    }

    public int getDp() {
        return mDp;
    }

    public void setDp(int mDp) {
        this.mDp = mDp;
    }

    public int getResource() {
        return mResource;
    }

    public void setResource(int mResource) {
        this.mResource = mResource;
    }

    public static DimenHolder fromPixel(int pixel) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.mPixel = pixel;
        return dimenHolder;
    }

    public static DimenHolder fromDp(int dp) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.mDp = dp;
        return dimenHolder;
    }

    public static DimenHolder fromResource(@DimenRes int resource) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.mResource = resource;
        return dimenHolder;
    }

    public int asPixel(Context ctx) {
        if (mPixel != Integer.MIN_VALUE) {
            return mPixel;
        } else if (mDp != Integer.MIN_VALUE) {
            return (int) UIUtils.convertDpToPixel(mDp, ctx);
        } else if (mResource != Integer.MIN_VALUE) {
            return ctx.getResources().getDimensionPixelSize(mResource);
        }
        return 0;
    }
}
