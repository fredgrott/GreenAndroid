/*
 * Copyright (C) 2015 Jorge Castillo Pérez
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shareme.greenandroid.fillableloaders.clippingtransforms;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.view.View;

/**
 * @author truizlop
 * @since 13/08/15
 */
@SuppressWarnings("unused")
public class SquareClippingTransform implements ClippingTransform {

  private int width, height;
  private int squareSize = 24;

  public SquareClippingTransform() {
  }

  public SquareClippingTransform(int squareSize) {
    this.squareSize = squareSize;
  }

  @Override public void transform(Canvas canvas, float currentFillPhase, View view) {
    cacheDimensions(view.getWidth(), view.getHeight());
    Path path = buildClippingPath();
    path.offset(0, height * -currentFillPhase);
    canvas.clipPath(path, Region.Op.DIFFERENCE);
  }

  private void cacheDimensions(int width, int height) {
    if (this.width == 0 || this.height == 0) {
      this.width = width;
      this.height = height;
    }
  }

  protected Path buildClippingPath() {

    Path squaresPath = new Path();
    int numSquares = (int) Math.ceil(width / (2 * squareSize));
    int startingHeight = height;
    int lowerHeight = startingHeight - squareSize;
    squaresPath.moveTo(0, startingHeight);

    for (int i = 0; i < numSquares; i++) {
      squaresPath.lineTo((2 * i + 1) * squareSize, startingHeight);
      squaresPath.lineTo((2 * i + 1) * squareSize, lowerHeight);
      squaresPath.lineTo(2 * (i + 1) * squareSize, lowerHeight);
      squaresPath.lineTo(2 * (i + 1) * squareSize, startingHeight);
    }

    squaresPath.lineTo(width, startingHeight);
    squaresPath.lineTo(width, 0);
    squaresPath.lineTo(0, 0);
    squaresPath.close();

    return squaresPath;
  }
}
