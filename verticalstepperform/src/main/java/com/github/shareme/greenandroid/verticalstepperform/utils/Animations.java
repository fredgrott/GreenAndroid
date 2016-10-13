/*
 Copyright 2016 Julio Ernesto RodrÃ­guez CabaÃ±as
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
package com.github.shareme.greenandroid.verticalstepperform.utils;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

// http://stackoverflow.com/a/13381228/3891038 + modifications
public class Animations {
    public static void slideDown(final View v) {
        if(v.getVisibility() != View.VISIBLE) {

            v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            setHeight(v, 1);
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int newHeight = interpolatedTime == 1
                            ? WindowManager.LayoutParams.WRAP_CONTENT
                            : (int) (targetHeight * interpolatedTime);
                    setHeight(v, newHeight);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            // 1dp/ms
            a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 2);
            v.startAnimation(a);
        }
    }

    public static void slideUp(final View v) {
        if(v.getVisibility() == View.VISIBLE) {

            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime != 1) {
                        int newHeight = initialHeight - (int) (initialHeight * interpolatedTime);
                        setHeight(v, newHeight);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }

            };

            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            // 1dp/ms
            a.setDuration(((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)) * 2);
            v.startAnimation(a);
        }
    }

    protected static void setHeight(View v, int newHeight) {
        //v.getLayoutParams().height = newHeight;
        v.setLayoutParams(new LinearLayout.LayoutParams(v.getLayoutParams().width, newHeight));
    }

}
