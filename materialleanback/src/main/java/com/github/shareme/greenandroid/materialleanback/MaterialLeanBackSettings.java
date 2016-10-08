/*
 Copyright 2015 florent37, Inc.
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
package com.github.shareme.greenandroid.materialleanback;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Created by florentchampigny on 28/08/15.
 */
@SuppressWarnings("unused")
public class MaterialLeanBackSettings {

    public Integer titleColor;
    public int titleSize;
    public boolean animateCards;
    public boolean overlapCards;
    public int elevationReduced;
    public int elevationEnlarged;

    public Integer backgroundId;
    public Float backgroundOverlay;
    public Integer backgroundOverlayColor;

    public Integer paddingTop;
    public Integer paddingBottom;
    public Integer paddingLeft;
    public Integer paddingRight;

    public Integer lineSpacing;

    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaterialLeanBack);

            {
                if(styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_titleColor))
                    titleColor = styledAttrs.getColor(R.styleable.MaterialLeanBack_mlb_titleColor, -1);
                titleSize = styledAttrs.getDimensionPixelSize(R.styleable.MaterialLeanBack_mlb_titleSize, -1);
                animateCards = styledAttrs.getBoolean(R.styleable.MaterialLeanBack_mlb_animateCards, true);
                overlapCards = styledAttrs.getBoolean(R.styleable.MaterialLeanBack_mlb_overlapCards, true);
                elevationEnlarged = styledAttrs.getInteger(R.styleable.MaterialLeanBack_mlb_cardElevationEnlarged, 8);
                elevationReduced = styledAttrs.getInteger(R.styleable.MaterialLeanBack_mlb_cardElevationReduced, 5);

                {
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingTop))
                        paddingTop = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingTop, -1);
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingBottom))
                        paddingBottom = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingBottom, -1);
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingLeft))
                        paddingLeft = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingLeft, -1);
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_paddingRight))
                        paddingRight = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_paddingRight, -1);
                }

                {
                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_background))
                        backgroundId = styledAttrs.getResourceId(R.styleable.MaterialLeanBack_mlb_background, -1);

                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_backgroundOverlay))
                        backgroundOverlay = styledAttrs.getFloat(R.styleable.MaterialLeanBack_mlb_backgroundOverlay, -1);

                    if (styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_backgroundOverlayColor))
                        backgroundOverlayColor = styledAttrs.getColor(R.styleable.MaterialLeanBack_mlb_backgroundOverlayColor, -1);
                }

                if(styledAttrs.hasValue(R.styleable.MaterialLeanBack_mlb_lineSpacing))
                    lineSpacing = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialLeanBack_mlb_lineSpacing, -1);
            }

            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
