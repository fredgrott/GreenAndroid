/*
Copyright 2016 florent37, Inc.
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
package com.github.shareme.greenandroid.fiftyshadesof.viewstate;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.github.shareme.greenandroid.fiftyshadesof.GreyDrawable;

/**
 * Created by f.champigny on 30/08/16.
 */
public abstract class ViewState<V extends View> {
    V view;
    Drawable background;
    protected boolean darker;

    public ViewState(V view) {
        this.view = view;
        init();
    }

    protected void init() {
        this.background = view.getBackground();
    }

    protected void restore() {
    }

    protected void restoreBackground() {
        this.view.setBackgroundDrawable(background);
    }

    public void start(boolean fadein) {
        GreyDrawable greyDrawable = new GreyDrawable();
        this.view.setBackgroundDrawable(greyDrawable);
        greyDrawable.setFadein(fadein);
        greyDrawable.start(view, darker);
    }

    public void stop() {
        Drawable drawable = this.view.getBackground();
        if(drawable instanceof GreyDrawable){
            GreyDrawable greyDrawable = (GreyDrawable)drawable;
            greyDrawable.stop(new GreyDrawable.Callback(){
                @Override
                public void onFadeOutStarted() {
                    restore();
                }

                @Override
                public void onFadeOutFinished() {
                    restoreBackground();
                }
            });
        } else {
            restore();
        }
    }
}
