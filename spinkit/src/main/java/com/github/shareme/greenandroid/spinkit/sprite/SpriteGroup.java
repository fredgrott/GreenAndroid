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

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.github.shareme.greenandroid.spinkit.animation.AnimationUtils;


/**
 * Created by ybq.
 */
@SuppressWarnings("unused")
public abstract class SpriteGroup extends Sprite {

    private Sprite[] sprites;

    private int color;

    public SpriteGroup() {
        sprites = onCreateChild();
        initCallBack();
        onChildCreated(sprites);
    }

    private void initCallBack() {
        if (sprites != null) {
            for (Sprite sprite : sprites) {
                sprite.setCallback(this);
            }
        }
    }

    public void onChildCreated(Sprite... sprites) {

    }

    public int getChildCount() {
        return sprites == null ? 0 : sprites.length;
    }

    public Sprite getChildAt(int index) {
        return sprites == null ? null : sprites[index];
    }


    @Override
    public void setColor(int color) {
        this.color = color;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setColor(color);
        }
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawChild(canvas);
    }

    public void drawChild(Canvas canvas) {
        if (sprites != null) {
            for (Sprite sprite : sprites) {
                int count = canvas.save();
                sprite.draw(canvas);
                canvas.restoreToCount(count);
            }
        }
    }


    @Override
    protected void drawSelf(Canvas canvas) {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        for (Sprite sprite : sprites) {
            sprite.setBounds(bounds);
        }
    }


    @Override
    public void start() {
        super.start();
        AnimationUtils.start(sprites);
    }

    @Override
    public void stop() {
        super.stop();
        AnimationUtils.stop(sprites);
    }


    @Override
    public boolean isRunning() {
        return AnimationUtils.isRunning(sprites) ||super.isRunning();
    }

    public abstract Sprite[] onCreateChild();

    @Override
    public ValueAnimator getAnimation() {
        return null;
    }
}
