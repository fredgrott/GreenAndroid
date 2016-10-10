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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

class ParticleField extends View {

	private ArrayList<Particle> mParticles;

	public ParticleField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ParticleField(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ParticleField(Context context) {
		super(context);
	}

	public void setParticles(ArrayList<Particle> particles) {
		mParticles = particles;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw all the particles
		for (int i=0; i<mParticles.size(); i++) {
			mParticles.get(i).draw(canvas);			
		}
	}
}
