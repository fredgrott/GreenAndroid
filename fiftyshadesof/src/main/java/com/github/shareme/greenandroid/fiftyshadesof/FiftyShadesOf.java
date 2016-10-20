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
package com.github.shareme.greenandroid.fiftyshadesof;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.shareme.greenandroid.fiftyshadesof.viewstate.ImageViewState;
import com.github.shareme.greenandroid.fiftyshadesof.viewstate.TextViewState;
import com.github.shareme.greenandroid.fiftyshadesof.viewstate.ViewState;

import java.util.HashMap;

/**
 * Created by florentchampigny on 29/08/16.
 */
@SuppressWarnings("unused")
public class FiftyShadesOf {
    private final Context context;

    private HashMap<View, ViewState> viewsState;

    boolean fadein = true;

    public FiftyShadesOf(Context context) {
        this.context = context;
        this.viewsState = new HashMap<>();
    }

    public static FiftyShadesOf with(Context context) {
        return new FiftyShadesOf(context);
    }

    public FiftyShadesOf on(int... viewsId) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            for (int view : viewsId) {
                add(activity.findViewById(view));
            }
        }
        return this;
    }

    public FiftyShadesOf fadein(boolean fadein) {
        this.fadein = fadein;
        return this;
    }

    private void add(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            viewsState.put(view, new TextViewState(textView));
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            viewsState.put(view, new ImageViewState(imageView));
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; ++i) {
                View child = viewGroup.getChildAt(i);
                add(child);
            }
        }
    }

    public FiftyShadesOf on(View... views) {
        for (View view : views)
            add(view);
        return this;
    }

    public FiftyShadesOf except(View... views) {
        for (View view : views) {
            this.viewsState.remove(view);
        }
        return this;
    }

    public FiftyShadesOf start() {
        for (ViewState viewState : viewsState.values()) {
            viewState.start(fadein);
        }
        return this;
    }

    public FiftyShadesOf stop() {
        for (ViewState viewState : viewsState.values()) {
            viewState.stop();
        }
        return this;
    }
}
