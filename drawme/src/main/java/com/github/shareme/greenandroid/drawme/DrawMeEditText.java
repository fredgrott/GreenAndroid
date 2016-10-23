/*
Copyright 2016 RafaÅ‚ KobyÅ‚ko
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
package com.github.shareme.greenandroid.drawme;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.github.shareme.greenandroid.drawme.delegate.DrawMe;
import com.github.shareme.greenandroid.drawme.delegate.DrawMeShapeText;

@SuppressWarnings("unused")
public class DrawMeEditText extends AppCompatEditText {
  private final DrawMe drawMe;

  public DrawMeEditText(Context context) {
    super(context);
    drawMe = new DrawMeShapeText(context, this, null);
  }

  public DrawMeEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    drawMe = new DrawMeShapeText(context, this, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int[] size = drawMe.onMeasure(widthMeasureSpec, heightMeasureSpec);
    super.onMeasure(size[0], size[1]);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    drawMe.onLayout(changed, left, top, right, bottom);
  }
}
