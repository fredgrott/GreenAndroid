/*
 Copyright 2014 Plattysoft
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
package com.github.shareme.greenandroid.leonids;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.github.shareme.greenandroid.leonids.modifiers.ParticleModifier;

import java.util.List;

public class Particle {

	protected Bitmap mImage;
	
	public float mCurrentX;
	public float mCurrentY;
	
	public float mScale = 1f;
	public int mAlpha = 255;
	
	public float mInitialRotation = 0f;
	
	public float mRotationSpeed = 0f;
	
	public float mSpeedX = 0f;
	public float mSpeedY = 0f;

	public float mAccelerationX;
	public float mAccelerationY;

	private Matrix mMatrix;
	private Paint mPaint;

	private float mInitialX;
	private float mInitialY;

	private float mRotation;

	private long mTimeToLive;

	protected long mStartingMilisecond;

	private int mBitmapHalfWidth;
	private int mBitmapHalfHeight;

	private List<ParticleModifier> mModifiers;


	protected Particle() {		
		mMatrix = new Matrix();
		mPaint = new Paint();
	}
	
	public Particle (Bitmap bitmap) {
		this();
		mImage = bitmap;
	}

	public void init() {
		mScale = 1;
		mAlpha = 255;	
	}
	
	public void configure(long timeToLive, float emiterX, float emiterY) {
		mBitmapHalfWidth = mImage.getWidth()/2;
		mBitmapHalfHeight = mImage.getHeight()/2;
		
		mInitialX = emiterX - mBitmapHalfWidth;
		mInitialY = emiterY - mBitmapHalfHeight;
		mCurrentX = mInitialX;
		mCurrentY = mInitialY;
		
		mTimeToLive = timeToLive;
	}

	public boolean update (long miliseconds) {
		long realMiliseconds = miliseconds - mStartingMilisecond;
		if (realMiliseconds > mTimeToLive) {
			return false;
		}
		mCurrentX = mInitialX+mSpeedX*realMiliseconds+mAccelerationX*realMiliseconds*realMiliseconds;
		mCurrentY = mInitialY+mSpeedY*realMiliseconds+mAccelerationY*realMiliseconds*realMiliseconds;
		mRotation = mInitialRotation + mRotationSpeed*realMiliseconds/1000;
		for (int i=0; i<mModifiers.size(); i++) {
			mModifiers.get(i).apply(this, realMiliseconds);
		}
		return true;
	}
	
	public void draw (Canvas c) {
		mMatrix.reset();
		mMatrix.postRotate(mRotation, mBitmapHalfWidth, mBitmapHalfHeight);
		mMatrix.postScale(mScale, mScale, mBitmapHalfWidth, mBitmapHalfHeight);
		mMatrix.postTranslate(mCurrentX, mCurrentY);
		mPaint.setAlpha(mAlpha);		
		c.drawBitmap(mImage, mMatrix, mPaint);
	}

	public Particle activate(long startingMilisecond, List<ParticleModifier> modifiers) {
		mStartingMilisecond = startingMilisecond;
		// We do store a reference to the list, there is no need to copy, since the modifiers do not carte about states 
		mModifiers = modifiers;
		return this;
	}
}
