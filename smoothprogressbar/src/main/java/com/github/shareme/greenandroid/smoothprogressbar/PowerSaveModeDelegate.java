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
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Created by castorflex on 9/12/15.
 */
public class PowerSaveModeDelegate implements PBDelegate {
  private static final long REFRESH_RATE = TimeUnit.SECONDS.toMillis(1L);

  private final CircularProgressDrawable mParent;
  private int mCurrentRotation;

  public PowerSaveModeDelegate(@NonNull CircularProgressDrawable parent) {
    mParent = parent;
  }

  @Override
  public void draw(Canvas canvas, Paint paint) {
    canvas.drawArc(mParent.getDrawableBounds(), mCurrentRotation, 300, false, paint);
  }

  @Override
  public void start() {
    mParent.invalidate();

    mParent.scheduleSelf(mRunnable, SystemClock.uptimeMillis() + REFRESH_RATE);
  }

  @Override
  public void stop() {
    mParent.unscheduleSelf(mRunnable);
  }

  @Override
  public void progressiveStop(CircularProgressDrawable.OnEndListener listener) {
    mParent.stop();
  }

  private final Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      mCurrentRotation += 50;
      mCurrentRotation %= 360;

      if (mParent.isRunning())
        mParent.scheduleSelf(this, SystemClock.uptimeMillis() + REFRESH_RATE);

      mParent.invalidate();
    }
  };
}
