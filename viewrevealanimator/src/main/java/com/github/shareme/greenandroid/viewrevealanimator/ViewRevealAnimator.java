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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;

import timber.log.Timber;

/**
 * Created by alessandro crugnola on 14/11/14.
 */
@SuppressWarnings("unused")
public class ViewRevealAnimator extends FrameLayout {
    public interface OnViewChangedListener {
        void onViewChanged(int previousIndex, int currentIndex);
    }

    public interface OnViewAnimationListener {
        void onViewAnimationStarted(int previousIndex, int currentIndex);

        void onViewAnimationCompleted(int previousIndex, int currentIndex);
    }

    private static final String TAG = "ViewRevealAnimator";
    protected static final boolean DBG = false;
    int mWhichChild = 0;
    boolean mFirstTime = true;
    boolean mAnimateFirstTime = true;
    int mAnimationDuration;
    Animation mInAnimation;
    Animation mOutAnimation;
    Interpolator mInterpolator;
    OnViewChangedListener mViewChangedListener;
    OnViewAnimationListener mViewAnimationListener;
    RevealAnimator mInstance;
    boolean mHideBeforeReveal;

    public int getViewRadius(final View view) {
        return Math.max(view.getWidth(), view.getHeight());
    }

    public static final double distance(@NonNull Point origin, @NonNull Point newPoint) {
        return Math.sqrt((origin.x - newPoint.x) * (origin.x - newPoint.x) + (origin.y - newPoint.y) * (origin.y - newPoint.y));
    }

    public ViewRevealAnimator(Context context) {
        this(context, null);
    }

    public ViewRevealAnimator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewRevealAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= 21) {
            mInstance = new LollipopRevealAnimatorImpl(this);
        } else {
            mInstance = new ICSRevealAnimatorImpl(this);
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewRelealAnimator, defStyleAttr, 0);

        int resourceIn = a.getResourceId(R.styleable.ViewRelealAnimator_android_inAnimation, 0);
        int resourceOut = a.getResourceId(R.styleable.ViewRelealAnimator_android_outAnimation, 0);
        boolean flag = a.getBoolean(R.styleable.ViewRelealAnimator_android_animateFirstView, true);
        int animationDuration = a.getInteger(R.styleable.ViewRelealAnimator_android_animationDuration, 400);
        boolean hideBeforeReveal = a.getBoolean(R.styleable.ViewRelealAnimator_vra_hideBeforeReveal, true);

        setInAnimation(context, resourceIn);
        setOutAnimation(context, resourceOut);
        setAnimateFirstView(flag);

        setAnimationDuration(animationDuration);
        setHideBeforeReveal(hideBeforeReveal);

        if (Build.VERSION.SDK_INT >= 21) {
            int resID =
                a.getResourceId(R.styleable.ViewRelealAnimator_android_interpolator, android.R.interpolator.accelerate_decelerate);
            Interpolator interpolator = AnimationUtils.loadInterpolator(context, resID);
            setInterpolator(interpolator);
        }

        a.recycle();
        initViewAnimator(context, attrs);
    }

    private void initViewAnimator(Context context, AttributeSet attrs) {
        if (attrs == null) {
            setMeasureAllChildren(true);
            return;
        }

        final TypedArray a = context.obtainStyledAttributes(
            attrs,
            R.styleable.ViewRelealAnimator);
        final boolean measureAllChildren = a.getBoolean(
            R.styleable.ViewRelealAnimator_android_measureAllChildren, true);
        setMeasureAllChildren(measureAllChildren);
        a.recycle();
    }

    public void setOnViewChangedListener(OnViewChangedListener listener) {
        mViewChangedListener = listener;
    }

    public void setOnViewAnimationListener(OnViewAnimationListener listener) {
        mViewAnimationListener = listener;
    }

    /**
     * Sets which child view will be displayed.
     *
     * @param whichChild the index of the child view to display
     */
    public void setDisplayedChild(int whichChild) {
        setDisplayedChild(whichChild, true, null);
    }

    public void setDisplayedChild(int whichChild, boolean animate) {
        setDisplayedChild(whichChild, animate, null);
    }

    public void setDisplayedChild(int whichChild, boolean animate, @Nullable Point origin) {
        if (DBG) {
            Timber.i(TAG, "setDisplayedChild, current: " + mWhichChild + ", next: " + whichChild);
        }

        if (whichChild == mWhichChild) {
            // same child
            return;
        }

        int mPreviousChild = mWhichChild;
        mWhichChild = whichChild;

        if (whichChild >= getChildCount()) {
            mWhichChild = 0;
        } else if (whichChild < 0) {
            mWhichChild = getChildCount() - 1;
        }
        boolean hasFocus = getFocusedChild() != null;
        showOnly(mPreviousChild, mWhichChild, animate, origin);
        if (hasFocus) {
            requestFocus(FOCUS_FORWARD);
        }
    }

    /**
     * Returns the index of the currently displayed child view.
     *
     * @return
     */
    public int getDisplayedChild() {
        return mWhichChild;
    }

    public void showNext() {
        setDisplayedChild(mWhichChild + 1);
    }

    public void showPrevious() {
        setDisplayedChild(mWhichChild - 1);
    }

    void showOnly(int previousChild, int childIndex, boolean animate, @Nullable Point origin) {
        if (DBG) {
            Timber.i(TAG, "showOnly: " + previousChild + " >> " + childIndex + ", animate: " + animate);
        }

        animate = animate && shouldAnimate();

        mFirstTime = false;

        if (!animate) {
            mInstance.showOnlyNoAnimation(previousChild, childIndex);
            onViewChanged(previousChild, childIndex);
        } else {
            mInstance.showOnly(previousChild, childIndex, origin);
        }
    }

    protected Point getViewCenter(final View targetView) {
        Point newPoint = new Point();
        newPoint.x = (targetView.getWidth()) / 2;
        newPoint.y = (targetView.getHeight()) / 2;
        if (DBG) {
            Timber.v(TAG, "getViewCenter: " + newPoint.x + ", " + newPoint.y);
        }
        return newPoint;
    }

    protected void onViewChanged(int prevIndex, int curIndex) {
        if (null != mViewChangedListener) {
            mViewChangedListener.onViewChanged(prevIndex, curIndex);
        }
    }

    protected void onAnimationStarted(int prevIndex, int curIndex) {
        if (null != mViewAnimationListener) {
            mViewAnimationListener.onViewAnimationStarted(prevIndex, curIndex);
        }
    }

    protected void onAnimationCompleted(int prevIndex, int curIndex) {
        if (null != mViewAnimationListener) {
            mViewAnimationListener.onViewAnimationCompleted(prevIndex, curIndex);
        }
    }

    public boolean isAnimating() {
        return mInstance.isAnimating();
    }

    private boolean shouldAnimate() {
        return mInstance.shouldAnimate();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (DBG) {
            Timber.i(TAG, "addView, index: " + index + ", current children: " + getChildCount());
        }
        super.addView(child, index, params);
        if (getChildCount() == 1) {
            child.setVisibility(View.VISIBLE);
        } else {
            child.setVisibility(View.GONE);
        }
        if (index >= 0 && mWhichChild >= index) {
            setDisplayedChild(mWhichChild + 1, false, null);
        }
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        mWhichChild = 0;
        mFirstTime = true;
    }

    @Override
    public void removeView(View view) {
        final int index = indexOfChild(view);
        if (index >= 0) {
            removeViewAt(index);
        }
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        final int childCount = getChildCount();
        if (childCount == 0) {
            mWhichChild = 0;
            mFirstTime = true;
        } else if (mWhichChild >= childCount) {
            setDisplayedChild(childCount - 1, false, null);
        } else if (mWhichChild == index) {
            setDisplayedChild(mWhichChild, false, null);
        }
    }

    public void removeViewInLayout(View view) {
        removeView(view);
    }

    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        if (getChildCount() == 0) {
            mWhichChild = 0;
            mFirstTime = true;
        } else if (mWhichChild >= start && mWhichChild < start + count) {
            setDisplayedChild(mWhichChild, false, null);
        }
    }

    public void removeViewsInLayout(int start, int count) {
        removeViews(start, count);
    }

    /**
     * Returns the current visible child
     *
     * @return
     */
    public View getCurrentView() {
        return getChildAt(mWhichChild);
    }

    /**
     * Returns the current animation used to animate a View that enters the screen.
     *
     * @return An Animation or null if none is set.
     * @see #setInAnimation(Animation)
     * @see #setInAnimation(Context, int)
     */
    public Animation getInAnimation() {
        return mInAnimation;
    }

    /**
     * Specifies the animation used to animate a View that enters the screen.
     *
     * @param inAnimation The animation started when a View enters the screen.
     * @see #getInAnimation()
     * @see #setInAnimation(Context, int)
     */
    public void setInAnimation(Animation inAnimation) {
        mInAnimation = inAnimation;
    }

    /**
     * Returns the current animation used to animate a View that exits the screen.
     *
     * @return An Animation or null if none is set.
     * @see #setOutAnimation(Animation)
     * @see #setOutAnimation(Context, int)
     */
    public Animation getOutAnimation() {
        return mOutAnimation;
    }

    /**
     * Specifies the animation used to animate a View that exit the screen.
     *
     * @param outAnimation The animation started when a View exit the screen.
     * @see #getOutAnimation()
     * @see #setOutAnimation(Context, int)
     */
    public void setOutAnimation(Animation outAnimation) {
        mOutAnimation = outAnimation;
    }

    /**
     * Specifies the animation used to animate a View that enters the screen.
     *
     * @param context    The application's environment.
     * @param resourceID The resource id of the animation.
     * @see #getInAnimation()
     * @see #setInAnimation(Animation)
     */
    public void setInAnimation(Context context, int resourceID) {
        setInAnimation(AnimationUtils.loadAnimation(context, resourceID));
    }

    /**
     * Specifies the animation used to animate a View that exit the screen.
     *
     * @param context    The application's environment.
     * @param resourceID The resource id of the animation.
     * @see #getOutAnimation()
     * @see #setOutAnimation(Animation)
     */
    public void setOutAnimation(Context context, int resourceID) {
        setOutAnimation(AnimationUtils.loadAnimation(context, resourceID));
    }

    public void setHideBeforeReveal(boolean value) {
        mHideBeforeReveal = value;
    }

    public boolean getHideBeforeReveal() {
        return mHideBeforeReveal;
    }

    public void setAnimationDuration(int value) {
        mAnimationDuration = value;
    }

    public int getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setInterpolator(final Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public boolean getAnimateFirstView() {
        return mAnimateFirstTime;
    }

    public void setAnimateFirstView(boolean animate) {
        mAnimateFirstTime = animate;
    }

    @Override
    public int getBaseline() {
        return (getCurrentView() == null) ? super.getBaseline() : getCurrentView().getBaseline();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(ViewAnimator.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(ViewAnimator.class.getName());
    }
}
