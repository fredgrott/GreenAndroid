/*
 MIT License

Copyright (c) 2016 Tango Agency
Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
package com.github.shareme.greenandroid.materialintroscreen.animations;

import android.support.annotation.AnimRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.shareme.greenandroid.materialintroscreen.animations.translations.NoTranslation;


@SuppressWarnings("WeakerAccess, unused")
public class ViewTranslationWrapper {
    private View view;

    private IViewTranslation enterTranslation;
    private IViewTranslation exitTranslation;
    private IViewTranslation defaultTranslation;
    private Animation errorAnimation;

    public ViewTranslationWrapper(View view) {
        this.view = view;

        this.enterTranslation = new NoTranslation();
        this.exitTranslation = new NoTranslation();
        this.setErrorAnimation(0);
    }

    /**
     * Set translation after passing first slide
     * @param enterTranslation new translation
     */
    public ViewTranslationWrapper setEnterTranslation(IViewTranslation enterTranslation) {
        this.enterTranslation = enterTranslation;
        return this;
    }

    /**
     * Set translation after passing last slide
     * @param exitTranslation new translation
     */
    public ViewTranslationWrapper setExitTranslation(IViewTranslation exitTranslation) {
        this.exitTranslation = exitTranslation;
        return this;
    }

    /**
     * Set default translation
     * @param defaultTranslation new translation
     */
    public ViewTranslationWrapper setDefaultTranslation(IViewTranslation defaultTranslation) {
        this.defaultTranslation = defaultTranslation;
        return this;
    }

    /**
     * Set view on error animation
     * @param errorAnimation new animation
     */
    public ViewTranslationWrapper setErrorAnimation(@AnimRes int errorAnimation) {
        if (errorAnimation != 0) {
            this.errorAnimation = AnimationUtils.loadAnimation(view.getContext(), errorAnimation);
        }
        return this;
    }

    public void enterTranslate(float percentage) {
        this.enterTranslation.translate(view, percentage);
    }

    public void exitTranslate(float percentage) {
        this.exitTranslation.translate(view, percentage);
    }

    public void defaultTranslate(float percentage) {
        this.defaultTranslation.translate(view, percentage);
    }

    public void error() {
        if (errorAnimation != null) {
            this.view.startAnimation(errorAnimation);
        }
    }
}