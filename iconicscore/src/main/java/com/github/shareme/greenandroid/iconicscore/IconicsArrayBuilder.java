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
package com.github.shareme.greenandroid.iconicscore;


import com.github.shareme.greenandroid.iconicscore.typeface.IIcon;

import java.util.ArrayList;

/**
 * Created by mikepenz on 30.06.15.
 */
@SuppressWarnings("unused")
public class IconicsArrayBuilder {
    private IconicsDrawable mIconBase;
    private ArrayList<Object> mIcons = new ArrayList<>();

    public IconicsArrayBuilder(IconicsDrawable iconicsDrawable) {
        this.mIconBase = iconicsDrawable;
    }

    public IconicsArrayBuilder add(IIcon icon) {
        mIcons.add(icon);
        return this;
    }

    public IconicsArrayBuilder add(String icon) {
        mIcons.add(icon);
        return this;
    }

    public IconicsArrayBuilder add(Character icon) {
        mIcons.add(icon);
        return this;
    }

    public IconicsDrawable[] build() {
        IconicsDrawable[] iconicsDrawables = new IconicsDrawable[mIcons.size()];

        for (int i = 0; i < mIcons.size(); i++) {
            if (mIcons.get(i) instanceof IIcon) {
                iconicsDrawables[i] = mIconBase.clone().icon((IIcon) mIcons.get(i));
            } else if (mIcons.get(i) instanceof Character) {
                iconicsDrawables[i] = mIconBase.clone().icon((Character) mIcons.get(i));
            } else if (mIcons.get(i) instanceof String) {
                iconicsDrawables[i] = mIconBase.clone().iconText((String) mIcons.get(i));
            }
        }

        return iconicsDrawables;
    }
}
