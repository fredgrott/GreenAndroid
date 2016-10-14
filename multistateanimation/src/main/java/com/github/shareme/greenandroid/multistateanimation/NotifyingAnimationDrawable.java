/*
 Copyright 2016 KeepSafe Inc.
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
package com.github.shareme.greenandroid.multistateanimation;

import android.graphics.drawable.AnimationDrawable;

import java.lang.ref.WeakReference;

/**
 * Extends AnimationDrawable to signal an event when the animation finishes.
 * This class behaves identically to a normal AnimationDrawable, but contains a method for
 * registering a callback that is called whenever the final frame of the animation is played.
 * If the animation is continuous, the callback will be called repeatedly while the animation
 * is running.
 *
 * @author AJ Alt
 */
@SuppressWarnings("unused")
public class NotifyingAnimationDrawable extends AnimationDrawable {
    public interface OnAnimationFinishedListener {
        void onAnimationFinished();
    }

    private boolean mFinished = false;
    private WeakReference<OnAnimationFinishedListener> mListener = new WeakReference<OnAnimationFinishedListener>(null);

    /**
     * @param drawable The frames data from animation will be copied into this instance. The animation object will be unchanged.
     */
    public NotifyingAnimationDrawable(AnimationDrawable drawable) {
        for (int i = 0; i < drawable.getNumberOfFrames(); i++) {
            addFrame(drawable.getFrame(i), drawable.getDuration(i));
        }
        setOneShot(drawable.isOneShot());
    }

    public NotifyingAnimationDrawable() {
        super();
    }

    /**
     * @return The registered animation listener, or null if none exists.
     */
    public OnAnimationFinishedListener getAnimationFinishedListener() {
        return mListener.get();
    }

    /**
     * Sets a listener that will be called when the last frame of the animation is rendered.
     * If the animation is continuous, the listener will be called repeatedly while the animation
     * is running.
     *
     * @param listener The listener to register.
     */
    public void setAnimationFinishedListener(OnAnimationFinishedListener listener) {
        this.mListener = new WeakReference<OnAnimationFinishedListener>(listener);
    }

    /**
     * Indicates whether the animation has ever finished.
     */
    public boolean isFinished() {
        return mFinished;
    }

    @Override
    public boolean selectDrawable(int idx) {
        boolean result = super.selectDrawable(idx);

        if (idx != 0 && idx == getNumberOfFrames() - 1) {
            if (!mFinished || !isOneShot()) {
                mFinished = true;
                OnAnimationFinishedListener listener = mListener.get();
                if (listener != null) {
                    listener.onAnimationFinished();
                }
            }
        }

        return result;
    }
}