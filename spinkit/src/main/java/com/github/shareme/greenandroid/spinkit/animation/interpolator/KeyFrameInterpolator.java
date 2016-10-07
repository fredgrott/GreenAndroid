/*
 Copyright ybq 2016
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
package com.github.shareme.greenandroid.spinkit.animation.interpolator;

import android.animation.TimeInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by ybq.
 */
@SuppressWarnings("unused")
public class KeyFrameInterpolator implements Interpolator {

    private TimeInterpolator interpolator;
    private float[] fractions;


    public static KeyFrameInterpolator easeInOut(float... fractions) {
        KeyFrameInterpolator interpolator = new KeyFrameInterpolator(Ease.inOut());
        interpolator.setFractions(fractions);
        return interpolator;
    }

    public KeyFrameInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setFractions(float... fractions) {
        this.fractions = fractions;
    }

    @Override
    public synchronized float getInterpolation(float input) {
        if (fractions.length > 1) {
            for (int i = 0; i < fractions.length - 1; i++) {
                float start = fractions[i];
                float end = fractions[i + 1];
                float duration = end - start;
                if (input > start && input < end) {
                    input = (input - start) / duration;
                    return start + (interpolator.getInterpolation(input)
                            * duration);
                }
            }
        }
        return interpolator.getInterpolation(input);
    }
}