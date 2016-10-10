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


import com.github.shareme.greenandroid.leonids.Particle;

@SuppressWarnings("unused")
public class AccelerationModifier implements ParticleModifier {

	private float mVelocityX;
	private float mVelocityY;

	public AccelerationModifier(float velocity, float angle) {
		float velocityAngleInRads = (float) (angle*Math.PI/180f);
		mVelocityX = (float) (velocity * Math.cos(velocityAngleInRads));
		mVelocityY = (float) (velocity * Math.sin(velocityAngleInRads));
	}

	@Override
	public void apply(Particle particle, long miliseconds) {
		particle.mCurrentX += mVelocityX*miliseconds*miliseconds;
		particle.mCurrentY += mVelocityY*miliseconds*miliseconds;
	}

}
