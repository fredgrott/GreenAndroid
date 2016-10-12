/**
 *  Copyright (c) 2013, Facebook, Inc.
 *  All rights reserved.
 *  Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree. An additional grant
 *  of patent rights can be found in the PATENTS file in the same directory.
 */

package com.github.shareme.greenandroid.rebound.core;

@SuppressWarnings("unused")
public class SteppingLooper extends SpringLooper {

  private boolean mStarted;
  private long mLastTime;

  @Override
  public void start() {
    mStarted = true;
    mLastTime = 0;
  }

  public boolean step(long interval) {
    if (mSpringSystem == null || !mStarted) {
      return false;
    }
    long currentTime = mLastTime + interval;
    mSpringSystem.loop(currentTime);
    mLastTime = currentTime;
    return mSpringSystem.getIsIdle();
  }

  @Override
  public void stop() {
    mStarted = false;
  }
}

