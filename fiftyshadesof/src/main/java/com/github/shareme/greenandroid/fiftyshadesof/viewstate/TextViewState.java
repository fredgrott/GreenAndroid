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

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by f.champigny on 30/08/16.
 */
@SuppressWarnings("unused")
public class TextViewState extends ViewState<TextView> {
    ColorStateList textColor;

    public TextViewState(TextView textView) {
        super(textView);
    }

    @Override
    protected void init() {
        super.init();
        this.textColor = view.getTextColors();
        this.darker = view.getTypeface() != null && view.getTypeface().isBold();
    }

    @Override
    protected void restore() {
        this.view.setTextColor(textColor);
    }

    @Override
    public void start(boolean fadein) {
        super.start(fadein);
        view.setTextColor(Color.TRANSPARENT);
    }
}
