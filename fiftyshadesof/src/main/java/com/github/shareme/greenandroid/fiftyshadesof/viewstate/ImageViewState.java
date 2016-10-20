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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by f.champigny on 30/08/16.
 */
public class ImageViewState extends ViewState<ImageView> {
    Drawable source;

    public ImageViewState(ImageView imageView) {
        super(imageView);
    }

    @Override
    protected void init() {
        super.init();
        this.source = view.getDrawable();
        view.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void restore() {
        this.view.setImageDrawable(source);
    }
}
