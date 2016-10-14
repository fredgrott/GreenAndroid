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
package com.github.shareme.greenandroid.shapeimageview.shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CircleShader extends ShaderHelper {
    private float center;
    private float bitmapCenterX;
    private float bitmapCenterY;
    private float borderRadius;
    private int bitmapRadius;

    public CircleShader() {
    }

    @Override
    public void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        square = true;
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
        canvas.drawCircle(center, center, borderRadius, borderPaint);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawCircle(bitmapCenterX, bitmapCenterY, bitmapRadius, imagePaint);
        canvas.restore();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        center = Math.round(viewWidth / 2f);
        borderRadius = Math.round((viewWidth - borderWidth) / 2f);
    }

    @Override
    public void calculate(int bitmapWidth, int bitmapHeight,
                          float width, float height,
                          float scale,
                          float translateX, float translateY) {
        bitmapCenterX = Math.round(bitmapWidth / 2f);
        bitmapCenterY = Math.round(bitmapHeight / 2f);
        bitmapRadius = Math.round(width / scale / 2f + 0.5f);
    }

    @Override
    public void reset() {
        bitmapRadius = 0;
        bitmapCenterX = 0;
        bitmapCenterY = 0;
    }
}