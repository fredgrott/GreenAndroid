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
package com.github.shareme.greenandroid.crossfader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.github.shareme.greenandroid.crossfader.R;
import com.github.shareme.greenandroid.crossfader.util.UIUtils;


/**
 * Created on 05.11.15
 *
 * @author github @androideveloper (Roland Yeghiazaryan)
 * @author github @suren1525 (Suren Khachatryan)
 */
@SuppressWarnings("unused")
public class GmailStyleCrossFadeSlidingPaneLayout extends CrossFadeSlidingPaneLayout {
    private boolean isEventHandled = false;

    public GmailStyleCrossFadeSlidingPaneLayout(Context context) {
        super(context);
    }

    public GmailStyleCrossFadeSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GmailStyleCrossFadeSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOutOfSecond(ev)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isOutOfSecond(ev)) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isOutOfSecond(MotionEvent ev) {
        if (MotionEvent.ACTION_UP == ev.getAction() || MotionEvent.ACTION_CANCEL == ev.getAction()) {
            isEventHandled = false;
        }
        LinearLayout mCrossFadeSecond = (LinearLayout) findViewById(R.id.second);
        if ((!isOpen() && ev.getAction() == MotionEvent.ACTION_DOWN && !UIUtils.isPointInsideView(ev.getRawX(), ev.getRawY(), mCrossFadeSecond)) || isEventHandled) {
            isEventHandled = true;
            return true;

        }
        return false;
    }
}