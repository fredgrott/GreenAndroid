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



public class AccelerationInitializer implements ParticleInitializer {

	private float mMinValue;
	private float mMaxValue;
	private int mMinAngle;
	private int mMaxAngle;

	public AccelerationInitializer(float minAcceleration, float maxAcceleration, int minAngle, int maxAngle) {
		mMinValue = minAcceleration;
		mMaxValue = maxAcceleration;
		mMinAngle = minAngle;
		mMaxAngle = maxAngle;
	}

	@Override
	public void initParticle(Particle p, Random r) {
		float angle = mMinAngle;
		if (mMaxAngle != mMinAngle) {
			angle = r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
		}
		float angleInRads = (float) (angle*Math.PI/180f);
		float value = r.nextFloat()*(mMaxValue-mMinValue)+mMinValue;
		p.mAccelerationX = (float) (value * Math.cos(angleInRads));
		p.mAccelerationY = (float) (value * Math.sin(angleInRads));
	}

}
