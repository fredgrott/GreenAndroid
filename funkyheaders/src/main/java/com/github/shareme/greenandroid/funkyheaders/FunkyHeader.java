/*
 Copyright(C) 2016 Intrusoft
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
package com.github.shareme.greenandroid.funkyheaders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Displays an arbitrary image with funky cuts.
 * The {@link FunkyHeader} class can load images from various sources (such as resources or bitmaps),
 * takes care of computing its measurement from the image so that it can be used in any layout manager,
 * and provides various display options such as scaling and tinting.
 *
 * @author Intruder Shanky
 * @since October 2016
 */
@SuppressWarnings("unused")
public class FunkyHeader extends View {

    private ScaleType scaleType;
    private float width;
    private float height;
    private float requiredWidth;
    private float requiredHeight;
    private Path path;
    private RectF viewBounds, scaleRect;
    private int colorTint;
    private int imageSource;
    private int x;
    private int y;
    private Paint paint;
    private Bitmap bitmap;
    private Context context;
    private final String LOG_TAG = "SQUINT_LOG";

    public enum ScaleType {
        CENTRE_CROP(0),
        FIT_XY(1);
        final int value;

        ScaleType(int value) {
            this.value = value;
        }
    }

    private static final ScaleType[] scaleTypeArray = {ScaleType.CENTRE_CROP, ScaleType.FIT_XY};

    public FunkyHeader(Context context) {
        super(context);
        init(context, null);
    }

    public FunkyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FunkyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FunkyHeader);
            try {
                imageSource = typedArray.getResourceId(R.styleable.FunkyHeader_src, -1);
                int scale = typedArray.getInt(R.styleable.FunkyHeader_scaleType, 0);
                scaleType = scaleTypeArray[scale];
                colorTint = typedArray.getColor(R.styleable.FunkyHeader_tint, Color.BLACK);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        } else {
            scaleType = ScaleType.CENTRE_CROP;
        }
        path = new Path();
        viewBounds = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        scaleRect = new RectF();
        this.context = context;
        if (imageSource != -1)
            try {
                bitmap = BitmapFactory.decodeResource(context.getResources(), imageSource);
            } catch (OutOfMemoryError error) {
                bitmap = null;
                Log.e(LOG_TAG, "Image is too large to process. " + error.getMessage());
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        if (colorTint != 0) {
            if (Color.alpha(colorTint) == 255)
                colorTint = Color.argb(100, Color.red(colorTint), Color.green(colorTint), Color.blue(colorTint));
            paint.setColor(colorTint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Log.d("T", width + "");
        if (bitmap != null && scaleType == ScaleType.CENTRE_CROP) {
            float ratioChange;
            if (width - bitmap.getWidth() < height - bitmap.getHeight())
                ratioChange = height / bitmap.getHeight();
            else ratioChange = width / requiredWidth;
            requiredHeight = bitmap.getHeight() * ratioChange;
            requiredWidth = bitmap.getWidth() * ratioChange;
            if (requiredWidth < width) {
                ratioChange = width / requiredWidth;
                requiredWidth *= ratioChange;
                requiredHeight *= ratioChange;
            }
            if (requiredHeight < height) {
                ratioChange = height / requiredHeight;
                requiredHeight *= ratioChange;
                requiredWidth *= ratioChange;
            }

            y = (int) ((requiredHeight / 2) - (height / 2));
            x = (int) ((requiredWidth / 2) - (width / 2));
            if (x > 0) x = -x;
            if (y > 0) y = -y;
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) requiredWidth, (int) requiredHeight, true);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        path = getWavePath(width, height, 80, 0, 3);
        canvas.drawPath(path, paint);
        path = getWavePath(width, height, 110, 60, 4);
        viewBounds.set(0, 0, width, height);
        canvas.clipPath(path);
        if (bitmap != null)
            if (scaleType == ScaleType.CENTRE_CROP) {
                scaleRect.set(x, y, x + requiredWidth, y + requiredHeight);
                canvas.clipRect(scaleRect);
                canvas.drawBitmap(bitmap, null, scaleRect, paint);
            } else {
                canvas.drawBitmap(bitmap, null, viewBounds, paint);
            }
        canvas.clipRect(viewBounds, Region.Op.UNION);
        canvas.drawPath(path, paint);
        path = getWavePath(width, height, 110, 20, 3);
        canvas.drawPath(path, paint);
    }

    private Path getWavePath(float width, float height, float amplitude, float shift, float divide) {
        Path path = new Path();
        float quadrant = height - amplitude;
        float x, y;
        path.moveTo(0, 0);
        path.lineTo(0, quadrant);
        for (int i = 0; i < width + 10; i = i + 10) {
            x = (float) i;
            y = quadrant + amplitude * (float) Math.sin(((i + 10) * Math.PI / 180) / divide + shift);
            path.lineTo(x, y);
        }
        path.lineTo(width, 0);
        path.close();
        return path;
    }

    /**
     * @param scaleType scaleType of the image on {@link FunkyHeader}
     */
    public void setScaleType(@NonNull ScaleType scaleType) {
        this.scaleType = scaleType;
        invalidate();
    }


    /**
     * @param bitmap is object of Scaled Bitmap
     */
    public void setBitmap(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    /**
     * @param resId is drawable resource Id of image
     */
    public void setImageSource(@DrawableRes int resId) {
        this.imageSource = resId;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), imageSource);
        } catch (OutOfMemoryError error) {
            bitmap = null;
            Log.e(LOG_TAG, "Image is too large to process. " + error.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        invalidate();
    }

    /**
     * @param color is image tint to provide theme effect. This is optional.
     */
    public void setColorTint(@ColorInt int color) {
        this.colorTint = color;
        if (colorTint != 0) {
            if (Color.alpha(colorTint) == 255)
                colorTint = Color.argb(55, Color.red(colorTint), Color.green(colorTint), Color.blue(colorTint));
            paint.setColor(colorTint);
        }
        invalidate();
    }
}
