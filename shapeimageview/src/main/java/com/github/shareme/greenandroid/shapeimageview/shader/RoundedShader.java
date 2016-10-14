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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.github.shareme.greenandroid.shapeimageview.R;


public class RoundedShader extends ShaderHelper {

    private final RectF borderRect = new RectF();
    private final RectF imageRect = new RectF();

    private int radius = 0;
    private int bitmapRadius;

    public RoundedShader() {
    }

    @Override
    public void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        borderPaint.setStrokeWidth(borderWidth*2);
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShaderImageView, defStyle, 0);
            radius = typedArray.getDimensionPixelSize(R.styleable.ShaderImageView_siRadius, radius);
            typedArray.recycle();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
        canvas.drawRoundRect(borderRect, radius, radius, borderPaint);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawRoundRect(imageRect, bitmapRadius, bitmapRadius, imagePaint);
        canvas.restore();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        borderRect.set(borderWidth, borderWidth, viewWidth - borderWidth, viewHeight - borderWidth);
    }

    @Override
    public void calculate(int bitmapWidth, int bitmapHeight,
                          float width, float height,
                          float scale,
                          float translateX, float translateY) {
        imageRect.set(-translateX, -translateY, bitmapWidth + translateX, bitmapHeight + translateY);
        bitmapRadius = Math.round(radius / scale);
    }

    @Override
    public void reset() {
        imageRect.set(0,0,0,0);
        bitmapRadius = 0;
    }
}