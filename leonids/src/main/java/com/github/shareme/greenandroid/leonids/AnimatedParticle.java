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

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

public class AnimatedParticle extends Particle {

	private AnimationDrawable mAnimationDrawable;
	private int mTotalTime;

	public AnimatedParticle(AnimationDrawable animationDrawable) {
		mAnimationDrawable = animationDrawable;
		mImage = ((BitmapDrawable) mAnimationDrawable.getFrame(0)).getBitmap();
		// If it is a repeating animation, calculate the time
		mTotalTime = 0;
		for (int i=0; i<mAnimationDrawable.getNumberOfFrames(); i++) {
			mTotalTime += mAnimationDrawable.getDuration(i);
		}
	}

	@Override
	public boolean update(long miliseconds) {
		boolean active = super.update(miliseconds);
		if (active) {
			long animationElapsedTime = 0;
			long realMiliseconds = miliseconds - mStartingMilisecond;
			if (realMiliseconds > mTotalTime) {
				if (mAnimationDrawable.isOneShot()) {
					return false;
				}
				else {
					realMiliseconds = realMiliseconds % mTotalTime;
				}
			}
			for (int i=0; i<mAnimationDrawable.getNumberOfFrames(); i++) {
				animationElapsedTime += mAnimationDrawable.getDuration(i);
				if (animationElapsedTime > realMiliseconds) {
					mImage = ((BitmapDrawable) mAnimationDrawable.getFrame(i)).getBitmap();
					break;
				}
			}
		}
		return active;
	}
}
