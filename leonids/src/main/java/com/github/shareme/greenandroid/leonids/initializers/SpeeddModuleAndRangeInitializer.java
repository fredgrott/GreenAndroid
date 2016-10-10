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
package com.github.shareme.greenandroid.leonids.initializers;

import com.github.shareme.greenandroid.leonids.Particle;

import java.util.Random;



public class SpeeddModuleAndRangeInitializer implements ParticleInitializer {

	private float mSpeedMin;
	private float mSpeedMax;
	private int mMinAngle;
	private int mMaxAngle;

	public SpeeddModuleAndRangeInitializer(float speedMin, float speedMax, int minAngle, int maxAngle) {
		mSpeedMin = speedMin;
		mSpeedMax = speedMax;
		mMinAngle = minAngle;
		mMaxAngle = maxAngle;
		// Make sure the angles are in the [0-360) range
		while (mMinAngle < 0) {
			mMinAngle+=360;
		}
		while (mMaxAngle < 0) {
			mMaxAngle+=360;
		}
		// Also make sure that mMinAngle is the smaller
		if (mMinAngle > mMaxAngle) {
			int tmp = mMinAngle;
			mMinAngle = mMaxAngle;
			mMaxAngle = tmp;
		}
	}

	@Override
	public void initParticle(Particle p, Random r) {
		float speed = r.nextFloat()*(mSpeedMax-mSpeedMin) + mSpeedMin;
		int angle;
		if (mMaxAngle == mMinAngle) {
			angle = mMinAngle;
		}
		else {
			angle = r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
		}
		float angleInRads = (float) (angle*Math.PI/180f);
		p.mSpeedX = (float) (speed * Math.cos(angleInRads));
		p.mSpeedY = (float) (speed * Math.sin(angleInRads));
	}

}
