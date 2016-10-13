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
package com.github.shareme.greenandroid.materialintroscreen.listeners;

import android.support.v4.view.ViewPager;

import com.github.shareme.greenandroid.materialintroscreen.adapter.SlidesAdapter;
import com.github.shareme.greenandroid.materialintroscreen.animations.ViewTranslationWrapper;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class ViewBehavioursOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private final SlidesAdapter adapter;

    private List<IPageSelectedListener> listeners = new ArrayList<>();
    private List<ViewTranslationWrapper> wrappers = new ArrayList<>();
    private List<IPageScrolledListener> pageScrolledListeners = new ArrayList<>();

    public ViewBehavioursOnPageChangeListener(SlidesAdapter adapter) {
        this.adapter = adapter;
    }

    public ViewBehavioursOnPageChangeListener registerPageSelectedListener(IPageSelectedListener pageSelectedListener) {
        listeners.add(pageSelectedListener);
        return this;
    }

    public ViewBehavioursOnPageChangeListener registerViewTranslationWrapper(ViewTranslationWrapper wrapper) {
        wrappers.add(wrapper);
        return this;
    }

    public ViewBehavioursOnPageChangeListener registerOnPageScrolled(IPageScrolledListener pageScrolledListener) {
        pageScrolledListeners.add(pageScrolledListener);
        return this;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isFirstSlide(position)) {
            for (ViewTranslationWrapper wrapper : wrappers) {
                wrapper.enterTranslate(positionOffset);
            }
        } else if (adapter.isLastSlide(position)) {
            for (ViewTranslationWrapper wrapper : wrappers) {
                wrapper.exitTranslate(positionOffset);
            }
        } else {
            for (ViewTranslationWrapper wrapper : wrappers) {
                wrapper.defaultTranslate(positionOffset);
            }
        }

        for (IPageScrolledListener pageScrolledListener : pageScrolledListeners) {
            pageScrolledListener.pageScrolled(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (IPageSelectedListener pageSelectedListener : listeners) {
            pageSelectedListener.pageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private boolean isFirstSlide(int position) {
        return position == 0;
    }
}