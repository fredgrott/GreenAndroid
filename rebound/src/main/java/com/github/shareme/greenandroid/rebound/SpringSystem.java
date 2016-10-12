/*
 *  Copyright (c) 2013, Facebook, Inc.
 *  All rights reserved.
 *  Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree. An additional grant
 *  of patent rights can be found in the PATENTS file in the same directory.
 *
 */

package com.github.shareme.greenandroid.rebound;

import com.github.shareme.greenandroid.rebound.core.BaseSpringSystem;
import com.github.shareme.greenandroid.rebound.core.SpringLooper;

/**
 * This is a wrapper for BaseSpringSystem that provides the convenience of automatically providing
 * the AndroidSpringLooper dependency in {@link SpringSystem#create}.
 */
public class SpringSystem extends BaseSpringSystem {

  /**
   * Create a new SpringSystem providing the appropriate constructor parameters to work properly
   * in an Android environment.
   * @return the SpringSystem
   */
  public static SpringSystem create() {
    return new SpringSystem(AndroidSpringLooperFactory.createSpringLooper());
  }

  private SpringSystem(SpringLooper springLooper) {
    super(springLooper);
  }

}