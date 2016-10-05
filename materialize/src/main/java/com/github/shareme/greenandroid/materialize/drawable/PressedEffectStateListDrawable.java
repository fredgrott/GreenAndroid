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
package com.github.shareme.greenandroid.materialize.drawable;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * http://stackoverflow.com/questions/7979440/android-cloning-a-drawable-in-order-to-make-a-statelistdrawable-with-filters
 * http://stackoverflow.com/users/2075875/malachiasz
 */
@SuppressWarnings("unused")
@SuppressLint("InlinedApi")
public class PressedEffectStateListDrawable extends StateListDrawable {

    private int color;
    private int selectionColor;

    public PressedEffectStateListDrawable(Drawable drawable, int color, int selectionColor) {
        super();

        drawable = drawable.mutate();

        addState(new int[]{android.R.attr.state_selected}, drawable);
        addState(new int[]{}, drawable);

        this.color = color;
        this.selectionColor = selectionColor;
    }

    public PressedEffectStateListDrawable(Drawable drawable, Drawable selectedDrawable, int color, int selectionColor) {
        super();

        drawable = drawable.mutate();
        selectedDrawable = selectedDrawable.mutate();

        addState(new int[]{android.R.attr.state_selected}, selectedDrawable);
        addState(new int[]{}, drawable);

        this.color = color;
        this.selectionColor = selectionColor;
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == android.R.attr.state_selected) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(selectionColor, PorterDuff.Mode.SRC_IN);
        } else {
            super.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}