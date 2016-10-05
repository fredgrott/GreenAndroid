/*
 * Copyright 2015 Mike Penz All rights reserved.
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shareme.greenandroid.materialize;

import android.app.Activity;
import android.view.ViewGroup;

import com.github.shareme.greenandroid.materialize.util.KeyboardUtil;
import com.github.shareme.greenandroid.materialize.view.IScrimInsetsLayout;


/**
 * Created by mikepenz on 07.07.15.
 */
@SuppressWarnings("unused")
public class Materialize {
    private final MaterializeBuilder mBuilder;
    private KeyboardUtil mKeyboardUtil = null;

    /**
     * the protected Constructor for the result
     *
     * @param materializeBuilder
     */
    protected Materialize(MaterializeBuilder materializeBuilder) {
        this.mBuilder = materializeBuilder;
    }

    /**
     * set the insetsFrameLayout to display the content in fullscreen
     * under the statusBar and navigationBar
     *
     * @param fullscreen
     */
    public void setFullscreen(boolean fullscreen) {
        if (mBuilder.mScrimInsetsLayout != null) {
            mBuilder.mScrimInsetsLayout.setTintStatusBar(!fullscreen);
            mBuilder.mScrimInsetsLayout.setTintNavigationBar(!fullscreen);
        }
    }

    /**
     * enable StatusBar tinting of the ScrimInsetsLayout
     *
     * @param tintStatusBar
     */
    public void setTintStatusBar(boolean tintStatusBar) {
        if (mBuilder.mScrimInsetsLayout != null) {
            mBuilder.mScrimInsetsLayout.setTintStatusBar(tintStatusBar);
        }
    }

    /**
     * enable navigationBar tinting of the ScrimInsetsLayout
     *
     * @param tintNavigationBar
     */
    public void setTintNavigationBar(boolean tintNavigationBar) {
        if (mBuilder.mScrimInsetsLayout != null) {
            mBuilder.mScrimInsetsLayout.setTintNavigationBar(tintNavigationBar);
        }
    }

    /**
     * Set the color for the statusBar
     *
     * @param statusBarColor
     */
    public void setStatusBarColor(int statusBarColor) {
        if (mBuilder.mScrimInsetsLayout != null) {
            mBuilder.mScrimInsetsLayout.setInsetForeground(statusBarColor);
            mBuilder.mScrimInsetsLayout.getView().invalidate();
        }
    }

    /**
     * get the materializeContentRoot Layout (ScrimInsetsFrameLayout)
     *
     * @return
     */
    public IScrimInsetsLayout getScrimInsetsFrameLayout() {
        return mBuilder.mScrimInsetsLayout;
    }

    /**
     * get the materializeContentRoot Layout
     *
     * @return
     */
    public ViewGroup getContent() {
        return mBuilder.mContentRoot;
    }


    /**
     * a helper method to enable the keyboardUtil for a specific activity
     * or disable it. note this will cause some frame drops because of the
     * listener.
     *
     * @param activity
     * @param enable
     */
    public void keyboardSupportEnabled(Activity activity, boolean enable) {
        if (getContent() != null && getContent().getChildCount() > 0) {
            if (mKeyboardUtil == null) {
                mKeyboardUtil = new KeyboardUtil(activity, getContent().getChildAt(0));
                mKeyboardUtil.disable();
            }

            if (enable) {
                mKeyboardUtil.enable();
            } else {
                mKeyboardUtil.disable();
            }
        }
    }
}
