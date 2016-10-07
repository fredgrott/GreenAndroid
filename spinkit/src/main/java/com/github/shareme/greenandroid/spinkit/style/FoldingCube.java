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
package com.github.shareme.greenandroid.spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.animation.LinearInterpolator;

import com.github.shareme.greenandroid.spinkit.animation.SpriteAnimatorBuilder;
import com.github.shareme.greenandroid.spinkit.sprite.RectSprite;
import com.github.shareme.greenandroid.spinkit.sprite.Sprite;
import com.github.shareme.greenandroid.spinkit.sprite.SpriteGroup;


/**
 * Created by ybq.
 */
@SuppressWarnings("unused")
public class FoldingCube extends SpriteGroup {

    @SuppressWarnings("FieldCanBeLocal")
    private boolean wrapContent = false;

    @Override
    public Sprite[] onCreateChild() {
        Cube[] cubes
                = new Cube[4];
        for (int i = 0; i < cubes.length; i++) {
            cubes[i] = new Cube();
            cubes[i].setAnimationDelay(300 * i - 1200);
        }
        return cubes;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int size = Math.min(bounds.width(), bounds.height());
        if (wrapContent) {
            size = (int) Math.sqrt(
                    (size
                            * size) / 2);
            int oW = (bounds.width() - size) / 2;
            int oH = (bounds.height() - size) / 2;
            bounds = new Rect(
                    bounds.left + oW,
                    bounds.top + oH,
                    bounds.right - oW,
                    bounds.bottom - oH
            );
        }

        int px = bounds.left + size / 2 + 1;
        int py = bounds.top + size / 2 + 1;
        for (int i = 0; i < getChildCount(); i++) {
            Sprite sprite = getChildAt(i);
            sprite.setDrawBounds(
                    bounds.left,
                    bounds.top,
                    px,
                    py
            );
            sprite.setPivotX(sprite.getDrawBounds().right);
            sprite.setPivotY(sprite.getDrawBounds().bottom);
        }
    }

    @Override
    public void drawChild(Canvas canvas) {

        Rect bounds = clipSquare(getBounds());
        for (int i = 0; i < getChildCount(); i++) {
            int count = canvas.save();
            canvas.rotate(45 + i * 90, bounds.centerX(), bounds.centerY());
            Sprite sprite = getChildAt(i);
            sprite.draw(canvas);
            canvas.restoreToCount(count);
        }
    }

    class Cube extends RectSprite {

        @Override
        public ValueAnimator getAnimation() {
            float fractions[] = new float[]{0f, 0.1f, 0.25f, 0.75f, 0.9f, 1f};
            return new SpriteAnimatorBuilder(this).
                    alpha(fractions, 0, 0, 255, 255, 0, 0).
                    rotateX(fractions, -180, -180, 0, 0, 0, 0).
                    rotateY(fractions, 0, 0, 0, 0, 180, 180).
                    duration(2400).
                    interpolator(new LinearInterpolator())
                    .build();
        }

    }
}
