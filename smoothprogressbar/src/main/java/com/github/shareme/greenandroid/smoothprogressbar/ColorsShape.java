/*
 Copyright 2013-2014 Antoine Merle
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
package com.github.shareme.greenandroid.smoothprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by castorflex on 3/5/14.
 */
@SuppressWarnings("unused")
public class ColorsShape extends Shape {

  private float mStrokeWidth;
  private int[] mColors;

  public ColorsShape(float strokeWidth, int[] colors) {
    mStrokeWidth = strokeWidth;
    mColors = colors;
  }

  public float getStrokeWidth() {
    return mStrokeWidth;
  }

  public void setStrokeWidth(float strokeWidth) {
    mStrokeWidth = strokeWidth;
  }

  public int[] getColors() {
    return mColors;
  }

  public void setColors(int[] colors) {
    mColors = colors;
  }

  @Override
  public void draw(Canvas canvas, Paint paint) {
    float ratio = 1f / mColors.length;
    int i = 0;
    paint.setStrokeWidth(mStrokeWidth);
    for (int color : mColors) {
      paint.setColor(color);
      canvas.drawLine(i * ratio * getWidth(), getHeight() / 2, ++i * ratio * getWidth(), getHeight() / 2, paint);
    }
  }
}
