/*
 Copyright 2016 Intrusoft
 Modifications Copyright 2016 Fred Grott(GrottWorkShop)

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
package com.github.shareme.greenandroid.blaze;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Displays an arbitrary image with continuous moving animation.
 * The {@link MotionView} class can load images from various sources (such as resources or bitmap),
 * takes care of computing its measurement from the image so that it can be used in any layout manager.
 *
 * @see ZoomView
 * <p>
 * Created by Intruder Shanky.
 * @since October 2016
 */
@SuppressWarnings("unused")
public class MotionView extends View {

    Bitmap imageSrc;
    int x1, y, x2;
    int HEIGHT, WIDTH, CHANGE;
    Rect rect1, rect2, layerRect;
    Handler handler;
    Paint paint;
    int moveFactor;
    Integer integer;
    Context context;

    public MotionView(Context context) {
        super(context);
        this.context = context;
    }

    public MotionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MotionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(final Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.blaze, 0, 0);
        try {
            integer = typeArray.getResourceId(R.styleable.blaze_src, R.drawable.place_holder);
            moveFactor = (int) typeArray.getFloat(R.styleable.blaze_translation_factor, 6);
        } finally {
            typeArray.recycle();
        }
        resetValues();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (HEIGHT > 0) {
            if (imageSrc != null) {
                rect1.set(x1, 0, x1 + CHANGE, HEIGHT);
                rect2.set(x2, 0, x2 + CHANGE, HEIGHT);
                canvas.drawBitmap(imageSrc, null, rect1, paint);
                canvas.drawBitmap(imageSrc, null, rect2, paint);
            }
        } else resetValues();
        super.onDraw(canvas);
        handler.postDelayed(runnable, 16);
    }

    public void updateValues() {
        if (x1 + CHANGE < 0)
            x1 = CHANGE;
        if (x2 + CHANGE < 0)
            x2 = CHANGE;
        x1 -= moveFactor;
        x2 -= moveFactor;
        invalidate();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateValues();
        }
    };

    public void resetValues() {
        WIDTH = getWidth();
        HEIGHT = getHeight();
        CHANGE = (int) (WIDTH * 1.5);
        rect1 = new Rect();
        rect2 = new Rect();
        layerRect = new Rect(0, 0, WIDTH, HEIGHT);
        paint = new Paint();
        x1 = 0;
        x2 = CHANGE;
        y = 0;
        handler = new Handler();
        setImageResource(integer);
    }

    /**
     * set motion factor of image.
     *
     * @param factor This is the speed of the animation. This value should be between 1 to 8 for best practice.
     */
    public void setTranslationFactor(int factor) {
        this.moveFactor = factor;
    }

    /**
     * set image to the {@link MotionView} from resource Id
     *
     * @param resId
     */
    public void setImageResource(Integer resId) {
        BitmapResolver.getBitmap(context, resId, WIDTH, HEIGHT, new BitmapResolver.FinalBitmap() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                imageSrc = bitmap;
            }
        });
    }

    /**
     * set image to the {@link MotionView} from bitmap
     *
     * @param bitmap
     */
    public void setImageBitmap(Bitmap bitmap) {
        this.imageSrc = bitmap;
    }

    /**
     * To set the color layer over the image,
     * this color should have some alpha value (color with transparency)
     *
     * @param color
     */
}
