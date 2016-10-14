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

import android.animation.Animator;
import android.support.annotation.CallSuper;

abstract class SimpleAnimatorListener implements Animator.AnimatorListener{
  private boolean mStarted = false;
  private boolean mCancelled = false;

  @Override
  @CallSuper
  public void onAnimationStart(Animator animation) {
    mCancelled = false;
    mStarted = true;
  }

  @Override
  public final void onAnimationEnd(Animator animation) {
    onPreAnimationEnd(animation);
    mStarted = false;
  }

  protected void onPreAnimationEnd(Animator animation) {
  }

  @Override
  @CallSuper
  public void onAnimationCancel(Animator animation) {
    mCancelled = true;
  }

  @Override
  public void onAnimationRepeat(Animator animation) {

  }

  public boolean isStartedAndNotCancelled() {
    return mStarted && !mCancelled;
  }
}
