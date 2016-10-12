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

package com.github.shareme.greenandroid.rebound.core;

/**
 * SpringSystemListener provides an interface for listening to events before and after each Physics
 * solving loop the BaseSpringSystem runs.
 */
public interface SpringSystemListener {

  /**
   * Runs before each pass through the physics integration loop providing an opportunity to do any
   * setup or alterations to the Physics state before integrating.
   * @param springSystem the BaseSpringSystem listened to
   */
  void onBeforeIntegrate(BaseSpringSystem springSystem);

  /**
   * Runs after each pass through the physics integration loop providing an opportunity to do any
   * setup or alterations to the Physics state after integrating.
   * @param springSystem the BaseSpringSystem listened to
   */
  void onAfterIntegrate(BaseSpringSystem springSystem);
}

