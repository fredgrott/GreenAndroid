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

import android.view.animation.Interpolator;

class Options {

  //params
  final Interpolator angleInterpolator;
  final Interpolator sweepInterpolator;
  final float borderWidth;
  final int[] colors;
  final float sweepSpeed;
  final float rotationSpeed;
  final int minSweepAngle;
  final int maxSweepAngle;
  @CircularProgressDrawable.Style final int style;

  public Options(Interpolator angleInterpolator,
                 Interpolator sweepInterpolator,
                 float borderWidth,
                 int[] colors,
                 float sweepSpeed,
                 float rotationSpeed,
                 int minSweepAngle,
                 int maxSweepAngle,
                 @CircularProgressDrawable.Style int style) {
    this.angleInterpolator = angleInterpolator;
    this.sweepInterpolator = sweepInterpolator;
    this.borderWidth = borderWidth;
    this.colors = colors;
    this.sweepSpeed = sweepSpeed;
    this.rotationSpeed = rotationSpeed;
    this.minSweepAngle = minSweepAngle;
    this.maxSweepAngle = maxSweepAngle;
    this.style = style;
  }


}
