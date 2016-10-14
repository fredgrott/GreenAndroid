/*
 Copyright 2015 Rey Pham.
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
package com.github.shareme.greenandroid.materialextras.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * A drawable that draw nothing.
 * @author Rey
 *
 */
@SuppressWarnings("unused")
public class BlankDrawable extends Drawable {

	private static BlankDrawable mInstance;
	
	public static BlankDrawable getInstance(){
		if(mInstance == null)
			synchronized (BlankDrawable.class) {
				if(mInstance == null)
					mInstance = new BlankDrawable();
			}
		
		return mInstance;
	}
	
	@Override
	public void draw(Canvas canvas) {}

	@Override
	public void setAlpha(int alpha) {}

	@Override
	public void setColorFilter(ColorFilter cf) {}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSPARENT;
	}

}
