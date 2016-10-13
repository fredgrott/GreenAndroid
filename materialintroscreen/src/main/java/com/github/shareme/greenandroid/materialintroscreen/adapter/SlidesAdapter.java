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
package com.github.shareme.greenandroid.materialintroscreen.adapter;


import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.github.shareme.greenandroid.materialintroscreen.LastEmptySlideFragment;
import com.github.shareme.greenandroid.materialintroscreen.SlideFragment;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class SlidesAdapter extends FragmentStatePagerAdapter {
    private ArrayList<SlideFragment> fragments = new ArrayList<>();

    public SlidesAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public SlideFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SlideFragment fragment = (SlideFragment) super.instantiateItem(container, position);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int getCalculatedCount() {
        if (isLastItemEmptySlide()) {
            return fragments.size() - 1;
        } else {
            return fragments.size();
        }
    }

    public void addItem(SlideFragment fragment) {
        fragments.add(getCalculatedCount(), fragment);
        this.notifyDataSetChanged();
    }

    public void addEmptySlide(LastEmptySlideFragment fragment) {
        fragments.add(fragment);
        this.notifyDataSetChanged();
    }

    public int getLastItemPosition() {
        return getCalculatedCount() - 1;
    }

    public boolean isLastSlide(int position) {
        return position == getCalculatedCount() - 1;
    }

    public boolean shouldFinish(int position) {
        return position == getCalculatedCount();
    }

    private boolean isLastItemEmptySlide() {
        return fragments.size() > 0 && fragments.get(fragments.size() - 1) instanceof LastEmptySlideFragment;
    }
}
