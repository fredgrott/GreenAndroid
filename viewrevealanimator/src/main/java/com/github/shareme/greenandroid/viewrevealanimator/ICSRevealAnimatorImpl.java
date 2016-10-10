/*The MIT License (MIT)

  Copyright (c) 2015 sephirot
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
package com.github.shareme.greenandroid.viewrevealanimator;

import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import static com.github.shareme.greenandroid.viewrevealanimator.ViewRevealAnimator.DBG;


/**
 * Created by alessandro on 01/02/15.
 */
public class ICSRevealAnimatorImpl extends RevealAnimatorImpl {
    ICSRevealAnimatorImpl(final ViewRevealAnimator animator) {
        super(animator);
    }

    @Override
    public void showOnly(final int previousChild, final int childIndex, @Nullable final Point origin) {
        if (DBG) {
            Log.i(TAG, "showOnly: " + previousChild + " >> " + childIndex);
        }

        parent.mInAnimation.setAnimationListener(new MyAnimationListener(previousChild, childIndex));

        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            if (i == childIndex) {
                if (parent.mInAnimation != null) {
                    child.startAnimation(parent.mInAnimation);
                }
                child.setVisibility(View.VISIBLE);
                parent.mFirstTime = false;
            } else {
                if (parent.mOutAnimation != null && child.getVisibility() == View.VISIBLE) {
                    child.startAnimation(parent.mOutAnimation);
                } else if (child.getAnimation() == parent.mInAnimation) {
                    parent.mInAnimation.setAnimationListener(null);
                    child.clearAnimation();
                }
                child.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showOnlyNoAnimation(final int previousIndex, final int childIndex) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            child.setVisibility(i == childIndex ? View.VISIBLE : View.GONE);
            if (child.getAnimation() == parent.mInAnimation) {
                parent.mInAnimation.setAnimationListener(null);
                child.clearAnimation();
            } else if (child.getAnimation() == parent.mOutAnimation) {
                child.clearAnimation();
            }
        }
    }

    @Override
    public boolean isAnimating() {
        return
            (parent.mInAnimation != null &&
                (parent.mInAnimation.hasStarted() && !parent.mInAnimation.hasEnded()))
                || (parent.mOutAnimation != null &&
                (parent.mOutAnimation.hasStarted() && !parent.mOutAnimation.hasEnded()));
    }

    @Override
    public boolean shouldAnimate() {
        return (!parent.mFirstTime || parent.mAnimateFirstTime)
            && !isAnimating()
            && null != parent.mInAnimation
            && null != parent.mOutAnimation;
    }

    class MyAnimationListener implements Animation.AnimationListener {
        final int prevIndex;
        final int childIndex;

        public MyAnimationListener(final int previousChild, final int childIndex) {
            this.prevIndex = previousChild;
            this.childIndex = childIndex;
        }

        @Override
        public void onAnimationStart(final Animation animation) {
            parent.onAnimationStarted(prevIndex, childIndex);
        }

        @Override
        public void onAnimationEnd(final Animation animation) {
            if (DBG) {
                Log.d(TAG, "onAnimationEnd");
            }
            parent.onAnimationCompleted(prevIndex, childIndex);
            parent.onViewChanged(prevIndex, childIndex);
        }

        @Override
        public void onAnimationRepeat(final Animation animation) {

        }
    }
}
