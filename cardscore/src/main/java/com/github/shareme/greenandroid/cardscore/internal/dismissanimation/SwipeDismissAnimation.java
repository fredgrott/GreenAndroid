/*
 * ******************************************************************************
 *   Copyright (c) 2014 Gabriele Mariotti.
 *   Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.github.shareme.greenandroid.cardscore.internal.dismissanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import com.github.shareme.greenandroid.cardscore.internal.Card;
import com.github.shareme.greenandroid.cardscore.view.base.CardViewWrapper;


/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SwipeDismissAnimation extends BaseDismissAnimation {


    public SwipeDismissAnimation(Context context) {
        this(context,true);
    }


    public SwipeDismissAnimation(Context context,boolean dismissRight) {
        super(context);
        mDismissRight = dismissRight;
    }

    @Override
    public void animate(final Card card, final CardViewWrapper cardView) {

        ((View)cardView).animate()
                .translationX(mDismissRight ? mListWidth : -mListWidth)
                .alpha(0)
                .setDuration(mAnimationTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        invokeCallbak((View)cardView);
                    }
                });
    }




}
