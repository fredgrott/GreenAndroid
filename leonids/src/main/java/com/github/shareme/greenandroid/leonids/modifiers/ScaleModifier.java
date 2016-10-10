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
package com.github.shareme.greenandroid.leonids.modifiers;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.github.shareme.greenandroid.leonids.Particle;

@SuppressWarnings("unused")
public class ScaleModifier implements ParticleModifier {

	private float mInitialValue;
	private float mFinalValue;
	private long mEndTime;
	private long mStartTime;
	private long mDuration;
	private float mValueIncrement;
	private Interpolator mInterpolator;

	public ScaleModifier (float initialValue, float finalValue, long startMilis, long endMilis, Interpolator interpolator) {
		mInitialValue = initialValue;
		mFinalValue = finalValue;
		mStartTime = startMilis;
		mEndTime = endMilis;
		mDuration = mEndTime - mStartTime;
		mValueIncrement = mFinalValue-mInitialValue;
		mInterpolator = interpolator;
	}
	
	public ScaleModifier (float initialValue, float finalValue, long startMilis, long endMilis) {
		this (initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
	}
	
	@Override
	public void apply(Particle particle, long miliseconds) {
		if (miliseconds < mStartTime) {
			particle.mScale = mInitialValue;
		}
		else if (miliseconds > mEndTime) {
			particle.mScale = mFinalValue;
		}
		else {
			float interpolaterdValue = mInterpolator.getInterpolation((miliseconds- mStartTime)*1f/mDuration);
			float newScale = mInitialValue + mValueIncrement*interpolaterdValue;
			particle.mScale = newScale;
		}
	}

}
