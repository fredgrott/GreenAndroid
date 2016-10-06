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
package com.github.shareme.greenandroid.actionitembadge.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.StateSet;

import com.github.shareme.greenandroid.actionitembadge.R;


/**
 * Created by mikepenz on 02.07.15.
 */
@SuppressWarnings("unused")
public class BadgeDrawableBuilder {
    private int mColor = 0;
    private int mColorPressed = 0;
    private int mCorners = -1;
    private int mStroke = -1;
    private int mStrokeColor = 0;

    public BadgeDrawableBuilder() {
    }

    public BadgeDrawableBuilder color(@ColorInt int color) {
        this.mColor = color;
        return this;
    }

    public BadgeDrawableBuilder colorPressed(@ColorInt int colorPressed) {
        this.mColorPressed = colorPressed;
        return this;
    }

    public BadgeDrawableBuilder corners(int corners) {
        this.mCorners = corners;
        return this;
    }

    public BadgeDrawableBuilder stroke(int stroke) {
        this.mStroke = stroke;
        return this;
    }

    public BadgeDrawableBuilder strokeColor(@ColorInt int strokeColor) {
        this.mStrokeColor = strokeColor;
        return this;
    }

    public StateListDrawable build(Context ctx) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        GradientDrawable normal = (GradientDrawable) ContextCompat.getDrawable(ctx, R.drawable.action_item_badge);
        GradientDrawable selected = (GradientDrawable) normal.getConstantState().newDrawable().mutate();

        normal.setColor(mColor);
        selected.setColor(mColorPressed);

        if (mStroke > -1) {
            normal.setStroke(mStroke, mStrokeColor);
            selected.setStroke(mStroke, mStrokeColor);
        }

        if (mCorners > -1) {
            normal.setCornerRadius(mCorners);
            selected.setCornerRadius(mCorners);
        }

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, selected);
        stateListDrawable.addState(StateSet.WILD_CARD, normal);

        return stateListDrawable;
    }
}
