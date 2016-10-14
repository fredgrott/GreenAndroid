/*
 Copyright 2015 Rey Pham.
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
package com.github.shareme.greenandroid.materialextras.drawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;

import com.github.shareme.greenandroid.materialextras.app.ThemeManager;


/**
 * Created by Rey on 5/27/2015.
 */
@SuppressWarnings("unused")
public class ThemeDrawable extends LevelListDrawable implements ThemeManager.OnThemeChangedListener {
    private int mStyleId;

    public ThemeDrawable(int styleId) {
        mStyleId = styleId;

        if(mStyleId != 0) {
            ThemeManager.getInstance().registerOnThemeChangedListener(this);
            initDrawables();
        }
    }

    private void initDrawables(){
        ThemeManager themeManager = ThemeManager.getInstance();
        int count = themeManager.getThemeCount();

        for(int i = 0; i < count; i++){
            Drawable drawable = themeManager.getContext().getResources().getDrawable(themeManager.getStyle(mStyleId, i));
            addLevel(i, i, drawable);
        }

        setLevel(themeManager.getCurrentTheme());
    }

    @Override
    public void onThemeChanged(ThemeManager.OnThemeChangedEvent event) {
        if(getLevel() != event.theme)
            setLevel(event.theme);
    }

}
