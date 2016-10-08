/*
 Copyright 2015-2019 dinus
 Modifications Copyright(C) 2016 Fred Grott(GrottWorkSpace)

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
package com.github.shareme.greenandroid.loadingdrawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

@SuppressWarnings("unused")
public class LoadingDrawable extends Drawable implements Animatable {
  private LoadingRenderer mLoadingRender;

  private final Callback mCallback = new Callback() {
    @Override
    public void invalidateDrawable(Drawable d) {
      invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable d, Runnable what, long when) {
      scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable d, Runnable what) {
      unscheduleSelf(what);
    }
  };

  public LoadingDrawable(LoadingRenderer loadingRender) {
    this.mLoadingRender = loadingRender;
    this.mLoadingRender.setCallback(mCallback);
  }

  @Override
  public void draw(Canvas canvas) {
    mLoadingRender.draw(canvas, getBounds());
  }

  @Override
  public void setAlpha(int alpha) {
    mLoadingRender.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    mLoadingRender.setColorFilter(cf);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public void start() {
    mLoadingRender.start();
  }

  @Override
  public void stop() {
    mLoadingRender.stop();
  }

  @Override
  public boolean isRunning() {
    return mLoadingRender.isRunning();
  }

  @Override
  public int getIntrinsicHeight() {
    return (int) (mLoadingRender.getHeight() + 1);
  }

  @Override
  public int getIntrinsicWidth() {
    return (int) (mLoadingRender.getWidth() + 1);
  }
}
