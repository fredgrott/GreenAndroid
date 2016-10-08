/*
 * Copyright 2014 Toxic Bakery
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shareme.greenandroid.viewpagertransforms;

import android.support.v4.view.ViewPager;
import android.view.View;

@SuppressWarnings("unused")
public class DrawFromBackTransformer implements ViewPager.PageTransformer {

	private static final float MIN_SCALE = 0.75f;

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();

		if (position < -1 || position > 1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);
			return;
		}

		if (position <= 0) { // [-1,0]
			// Use the default slide transition when moving to the left page
			// Fade the page out.
			view.setAlpha(1 + position);
			// Counteract the default slide transition
			view.setTranslationX(pageWidth * -position);

			// Scale the page down (between MIN_SCALE and 1)
			float scaleFactor = MIN_SCALE
					+ (1 - MIN_SCALE) * (1 - Math.abs(position));
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
			return;

		}

		if (position > 0.5 && position <= 1) { // (0,1]
			// Fade the page out.
			view.setAlpha(0);

			// Counteract the default slide transition
			view.setTranslationX(pageWidth * -position);
			return;
		}
		if (position > 0.3 && position <= 0.5) { // (0,1]
			// Fade the page out.
			view.setAlpha(1);

			// Counteract the default slide transition
			view.setTranslationX(pageWidth * position);

			float scaleFactor = MIN_SCALE;
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
			return;
		}
		if (position <= 0.3) { // (0,1]
			// Fade the page out.
			view.setAlpha(1);
			// Counteract the default slide transition
			view.setTranslationX(pageWidth * position);

			// Scale the page down (between MIN_SCALE and 1)
			float v = (float) (0.3 - position);
			v = v >= 0.25f ? 0.25f : v;
			float scaleFactor = MIN_SCALE + v;
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
		}
	}
}
