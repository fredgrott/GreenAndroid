/*
 Copyright 2015 siyamed
 Modifications Copyright(C0 2016 Fred Grott(GrottWorkShop)

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
package com.github.shareme.greenandroid.shapeimageview.mask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

@SuppressWarnings("unused")
public class PorterCircularImageView extends PorterImageView {
    private final RectF rect = new RectF();

    public PorterCircularImageView(Context context) {
        super(context);
        setup();
    }

    public PorterCircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public PorterCircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        setSquare(true);
    }

    @Override
    protected void paintMaskCanvas(Canvas maskCanvas, Paint maskPaint, int width, int height) {
        float radius = Math.min(width, height) / 2f;
        rect.set(0, 0, width, height);
        maskCanvas.drawCircle(rect.centerX(), rect.centerY(), radius, maskPaint);
    }
}