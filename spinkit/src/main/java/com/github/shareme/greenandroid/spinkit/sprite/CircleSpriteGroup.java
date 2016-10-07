/*
 Copyright ybq 2016
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
package com.github.shareme.greenandroid.spinkit.sprite;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by ybq.
 */
public abstract class CircleSpriteGroup extends SpriteGroup {

    @Override
    public void drawChild(Canvas canvas) {
        for (int i = 0; i < getChildCount(); i++) {
            Sprite sprite = getChildAt(i);
            int count = canvas.save();
            canvas.rotate(i * 360 / getChildCount(),
                    getBounds().centerX(),
                    getBounds().centerY());
            sprite.draw(canvas);
            canvas.restoreToCount(count);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int radius = (int) (bounds.width() * Math.PI / 3.6f / getChildCount());
        int left = bounds.centerX() - radius;
        int right = bounds.centerX() + radius;
        for (int i = 0; i < getChildCount(); i++) {
            Sprite sprite = getChildAt(i);
            sprite.setDrawBounds(left, bounds.top, right, bounds.top + radius * 2);
        }
    }
}
