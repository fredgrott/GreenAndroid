/*
Copyright 2016 fanrunqi
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
package com.github.shareme.greenandroid.waveprogressbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fanrunqi on 2016/7/6.
 */
@SuppressWarnings("unused")
public class WaveProgressView extends View {
    private int width;
    private int height;

    private Bitmap backgroundBitmap;

    private Path mPath;
    private Paint mPathPaint;

    private float mWaveHight = 20f;
    private float mWaveHalfWidth = 100f;
    private String mWaveColor = "#5be4ef";
    private  int  mWaveSpeed = 30;

    private Paint mTextPaint;
    private String currentText = "";
    private String mTextColor = "#FFFFFF";
    private int mTextSize = 41;

    private int maxProgress = 100;
    private int currentProgress = 0;
    private float CurY;

    private float distance = 0;
    private int RefreshGap = 10;

    private static final int INVALIDATE = 0X777;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INVALIDATE:
                    invalidate();
                    sendEmptyMessageDelayed(INVALIDATE,RefreshGap);
                    break;
            }
        }
    };


    public WaveProgressView(Context context) {
        this(context,null,0);
    }
    public WaveProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public WaveProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Init();
    }

    public void setCurrent(int currentProgress,String currentText) {
        this.currentProgress = currentProgress;
        this.currentText = currentText;
    }


    public void setMaxProgress(int maxProgress){
        this.maxProgress = maxProgress;
    }


    public void setText(String mTextColor,int mTextSize){
        this.mTextColor = mTextColor;
        this.mTextSize = mTextSize;
    }

    public void setWave(float mWaveHight,float mWaveWidth){
        this.mWaveHight = mWaveHight;
        this.mWaveHalfWidth = mWaveWidth/2;
    }


    public void setWaveColor(String mWaveColor){
        this.mWaveColor = mWaveColor;
    }

    public void setmWaveSpeed(int mWaveSpeed){
      this.mWaveSpeed = mWaveSpeed;
    }
    private void Init() {

        if(null==getBackground()){
            throw new IllegalArgumentException(String.format("background is null."));
        }else{
            backgroundBitmap = getBitmapFromDrawable(getBackground());
        }

        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        handler.sendEmptyMessageDelayed(INVALIDATE,100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        CurY = height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(backgroundBitmap!=null){

            canvas.drawBitmap(createImage(), 0, 0, null);
        }
    }
    private Bitmap createImage()
    {
        mPathPaint.setColor(Color.parseColor(mWaveColor));
        mTextPaint.setColor(Color.parseColor(mTextColor));
        mTextPaint.setTextSize(mTextSize);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap finalBmp = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(finalBmp);

        float CurMidY = height*(maxProgress-currentProgress)/maxProgress;
        if(CurY>CurMidY){
            CurY = CurY - (CurY-CurMidY)/10;
        }
        mPath.reset();
        mPath.moveTo(0-distance,CurY);

        int waveNum = width/((int)mWaveHalfWidth*4)+1;
        int multiplier = 0;
        for(int i =0;i<waveNum;i++){
         mPath.quadTo(mWaveHalfWidth*(multiplier+1)-distance,CurY-mWaveHight,mWaveHalfWidth*(multiplier+2)-distance,CurY);
         mPath.quadTo(mWaveHalfWidth*(multiplier+3)-distance,CurY+mWaveHight,mWaveHalfWidth*(multiplier+4)-distance,CurY);
         multiplier+=4;
        }
        distance +=mWaveHalfWidth/mWaveSpeed;
        distance = distance%(mWaveHalfWidth*4);

        mPath.lineTo(width,height);
        mPath.lineTo(0,height);
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);

        int min = Math.min(width,height);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,min,min,false);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        canvas.drawBitmap(backgroundBitmap,0,0,paint);

        canvas.drawText(currentText, width/2, height/2, mTextPaint);
        return finalBmp;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
