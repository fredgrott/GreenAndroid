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
public class Wave extends SpriteGroup {


    @Override
    public Sprite[] onCreateChild() {
        WaveItem[] waveItems = new WaveItem[5];
        for (int i = 0; i < waveItems.length; i++) {
            waveItems[i] = new WaveItem();
            waveItems[i].setAnimationDelay(-1200 + i * 100);
        }
        return waveItems;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int rw = bounds.width() / getChildCount();
        int width = bounds.width() / 5 * 3 / 5;
        for (int i = 0; i < getChildCount(); i++) {
            Sprite sprite = getChildAt(i);
            int l = bounds.left + i * rw + rw / 5;
            int r = l + width;
            sprite.setDrawBounds(l, bounds.top, r, bounds.bottom);
        }
    }

    class WaveItem extends RectSprite {
        @Override
        public ValueAnimator getAnimation() {
            float fractions[] = new float[]{0f, 0.2f, 0.4f, 1f};
            return new SpriteAnimatorBuilder(this).scaleY(fractions, 0.4f, 1f, 0.4f, 0.4f).
                    duration(1200).
                    easeInOut(fractions)
                    .build();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidateSelf();
        }
    }
}
