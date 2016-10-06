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
package com.github.shareme.greenandroid.itemanimators;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mikepenz on 08.01.16.
 */
@SuppressWarnings("unused")
public abstract class BaseScaleAnimator<T> extends BaseItemAnimator<T> {
    public void changeAnimation(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        final float prevTranslationX = ViewCompat.getTranslationX(oldHolder.itemView);
        final float prevTranslationY = ViewCompat.getTranslationY(oldHolder.itemView);
        final float prevValue = changeAnimationPrepare1(oldHolder);
        resetAnimation(oldHolder);
        int deltaX = (int) (toX - fromX - prevTranslationX);
        int deltaY = (int) (toY - fromY - prevTranslationY);
        // recover prev translation state after ending animation
        ViewCompat.setTranslationX(oldHolder.itemView, prevTranslationX);
        ViewCompat.setTranslationY(oldHolder.itemView, prevTranslationY);

        changeAnimationPrepare2(oldHolder, prevValue);
        if (newHolder != null) {
            // carry over translation values
            resetAnimation(newHolder);
            ViewCompat.setTranslationX(newHolder.itemView, -deltaX);
            ViewCompat.setTranslationY(newHolder.itemView, -deltaY);
            changeAnimationPrepare3(newHolder);
        }
    }

    /**
     * @param holder
     * @return the default value for the animatd attribute
     */
    abstract public float changeAnimationPrepare1(RecyclerView.ViewHolder holder);

    /**
     * animates the view to the previous default value
     *
     * @param holder
     * @param prevValue the previous value
     */
    abstract public void changeAnimationPrepare2(RecyclerView.ViewHolder holder, float prevValue);

    /**
     * resets the value
     *
     * @param holder
     */
    abstract public void changeAnimationPrepare3(RecyclerView.ViewHolder holder);
}
