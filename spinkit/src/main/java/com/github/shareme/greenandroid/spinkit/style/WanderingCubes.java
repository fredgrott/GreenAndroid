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
import android.graphics.Rect;

import com.github.shareme.greenandroid.spinkit.animation.SpriteAnimatorBuilder;
import com.github.shareme.greenandroid.spinkit.sprite.RectSprite;
import com.github.shareme.greenandroid.spinkit.sprite.Sprite;
import com.github.shareme.greenandroid.spinkit.sprite.SpriteGroup;


/**
 * Created by ybq.
 */
@SuppressWarnings("unused")
public class WanderingCubes extends SpriteGroup {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new Cube(),
                new Cube()
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        super.onChildCreated(sprites);
        sprites[1].setAnimationDelay(-900);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        bounds = clipSquare(bounds);
        super.onBoundsChange(bounds);
        for (int i = 0; i < getChildCount(); i++) {
            Sprite sprite = getChildAt(i);
            sprite.setDrawBounds(
                    bounds.left,
                    bounds.top,
                    bounds.left + bounds.width() / 4,
                    bounds.top + bounds.height() / 4
            );
        }
    }

    class Cube extends RectSprite {
        @Override
        public ValueAnimator getAnimation() {
            float fractions[] = new float[]{0f, 0.25f, 0.5f, 0.51f, 0.75f, 1f};
            return new SpriteAnimatorBuilder(this).
                    rotate(fractions, 0, -90, -179, -180, -270, -360).
                    translateXPercentage(fractions, 0f, 0.75f, 0.75f, 0.75f, 0f, 0f).
                    translateYPercentage(fractions, 0f, 0f, 0.75f, 0.75f, 0.75f, 0f).
                    scale(fractions, 1f, 0.5f, 1f, 1f, 0.5f, 1f).
                    duration(1800).
                    easeInOut(fractions)
                    .build();
        }
    }
}
