/*
 * Copyright 2016 Rodolfo Navalon.
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.shareme.greenandroid.shaperipple.model;

import android.graphics.Canvas;
import android.graphics.Path;

@SuppressWarnings("unused")
public class Triangle extends BaseShapeRipple {

    private Path path;

    public Triangle() {
        path = new Path();
    }
    @Override
    public void draw(Canvas canvas, int x, int y, float currentRadiusSize, int currentColor, int rippleIndex) {

        //TODO: need to improve performance due to drawing to canvas

        int mY = (y - (int)currentRadiusSize);
        int rX = (x - (int)currentRadiusSize);
        int lX = (x + (int)currentRadiusSize);
        int bY = (y + (int)currentRadiusSize);

        path.moveTo(x,  mY);
        path.lineTo(rX, bY);
        path.lineTo(lX, bY);
        path.close();
        shapePaint.setColor(currentColor);
        canvas.drawPath(path, shapePaint);
        path.reset();

    }
}
