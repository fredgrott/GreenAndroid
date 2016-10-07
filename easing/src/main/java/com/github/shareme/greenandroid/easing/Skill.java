/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 daimajia
 * Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.shareme.greenandroid.easing;

import com.github.shareme.greenandroid.easing.back.BackEaseIn;
import com.github.shareme.greenandroid.easing.back.BackEaseInOut;
import com.github.shareme.greenandroid.easing.back.BackEaseOut;
import com.github.shareme.greenandroid.easing.bounce.BounceEaseIn;
import com.github.shareme.greenandroid.easing.bounce.BounceEaseInOut;
import com.github.shareme.greenandroid.easing.bounce.BounceEaseOut;
import com.github.shareme.greenandroid.easing.circ.CircEaseIn;
import com.github.shareme.greenandroid.easing.circ.CircEaseOut;
import com.github.shareme.greenandroid.easing.circ.CircEaseInOut;
import com.github.shareme.greenandroid.easing.cubic.CubicEaseIn;
import com.github.shareme.greenandroid.easing.cubic.CubicEaseInOut;
import com.github.shareme.greenandroid.easing.cubic.CubicEaseOut;
import com.github.shareme.greenandroid.easing.elastic.ElasticEaseIn;
import com.github.shareme.greenandroid.easing.elastic.ElasticEaseInOut;
import com.github.shareme.greenandroid.easing.elastic.ElasticEaseOut;
import com.github.shareme.greenandroid.easing.expo.ExpoEaseIn;
import com.github.shareme.greenandroid.easing.expo.ExpoEaseInOut;
import com.github.shareme.greenandroid.easing.expo.ExpoEaseOut;
import com.github.shareme.greenandroid.easing.linear.Linear;
import com.github.shareme.greenandroid.easing.quad.QuadEaseIn;
import com.github.shareme.greenandroid.easing.quad.QuadEaseInOut;
import com.github.shareme.greenandroid.easing.quad.QuadEaseOut;
import com.github.shareme.greenandroid.easing.quint.QuintEaseIn;
import com.github.shareme.greenandroid.easing.quint.QuintEaseInOut;
import com.github.shareme.greenandroid.easing.quint.QuintEaseOut;
import com.github.shareme.greenandroid.easing.sine.SineEaseIn;
import com.github.shareme.greenandroid.easing.sine.SineEaseInOut;
import com.github.shareme.greenandroid.easing.sine.SineEaseOut;

@SuppressWarnings("unused")
public enum  Skill {

    BackEaseIn(BackEaseIn.class),
    BackEaseOut(BackEaseOut.class),
    BackEaseInOut(BackEaseInOut.class),

    BounceEaseIn(BounceEaseIn.class),
    BounceEaseOut(BounceEaseOut.class),
    BounceEaseInOut(BounceEaseInOut.class),

    CircEaseIn(CircEaseIn.class),
    CircEaseOut(CircEaseOut.class),
    CircEaseInOut(CircEaseInOut.class),

    CubicEaseIn(CubicEaseIn.class),
    CubicEaseOut(CubicEaseOut.class),
    CubicEaseInOut(CubicEaseInOut.class),

    ElasticEaseIn(ElasticEaseIn.class),
    ElasticEaseInOut(ElasticEaseInOut.class),
    ElasticEaseOut(ElasticEaseOut.class),

    ExpoEaseIn(ExpoEaseIn.class),
    ExpoEaseOut(ExpoEaseOut.class),
    ExpoEaseInOut(ExpoEaseInOut.class),

    QuadEaseIn(QuadEaseIn.class),
    QuadEaseOut(QuadEaseOut.class),
    QuadEaseInOut(QuadEaseInOut.class),

    QuintEaseIn(QuintEaseIn.class),
    QuintEaseOut(QuintEaseOut.class),
    QuintEaseInOut(QuintEaseInOut.class),

    SineEaseIn(SineEaseIn.class),
    SineEaseOut(SineEaseOut.class),
    SineEaseInOut(SineEaseInOut.class),

    Linear(Linear.class);


    private Class easingMethod;

    Skill(Class clazz) {
        easingMethod = clazz;
    }

    public BaseEasingMethod getMethod(float duration) {
        try {
            return (BaseEasingMethod)easingMethod.getConstructor(float.class).newInstance(duration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Can not init easingMethod instance");
        }
    }
}
