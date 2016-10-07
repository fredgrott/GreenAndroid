
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

package com.github.shareme.greenandroid.viewanim;


import com.github.shareme.greenandroid.viewanim.attention.BounceAnimator;
import com.github.shareme.greenandroid.viewanim.attention.FlashAnimator;
import com.github.shareme.greenandroid.viewanim.attention.PulseAnimator;
import com.github.shareme.greenandroid.viewanim.attention.RubberBandAnimator;
import com.github.shareme.greenandroid.viewanim.attention.ShakeAnimator;
import com.github.shareme.greenandroid.viewanim.attention.StandUpAnimator;
import com.github.shareme.greenandroid.viewanim.attention.SwingAnimator;
import com.github.shareme.greenandroid.viewanim.attention.TadaAnimator;
import com.github.shareme.greenandroid.viewanim.attention.WaveAnimator;
import com.github.shareme.greenandroid.viewanim.attention.WobbleAnimator;
import com.github.shareme.greenandroid.viewanim.bouncing_entrances.BounceInAnimator;
import com.github.shareme.greenandroid.viewanim.bouncing_entrances.BounceInDownAnimator;
import com.github.shareme.greenandroid.viewanim.bouncing_entrances.BounceInLeftAnimator;
import com.github.shareme.greenandroid.viewanim.bouncing_entrances.BounceInRightAnimator;
import com.github.shareme.greenandroid.viewanim.bouncing_entrances.BounceInUpAnimator;
import com.github.shareme.greenandroid.viewanim.fading_entrances.FadeInAnimator;
import com.github.shareme.greenandroid.viewanim.fading_entrances.FadeInDownAnimator;
import com.github.shareme.greenandroid.viewanim.fading_entrances.FadeInLeftAnimator;
import com.github.shareme.greenandroid.viewanim.fading_entrances.FadeInRightAnimator;
import com.github.shareme.greenandroid.viewanim.fading_entrances.FadeInUpAnimator;
import com.github.shareme.greenandroid.viewanim.fading_exits.FadeOutAnimator;
import com.github.shareme.greenandroid.viewanim.fading_exits.FadeOutDownAnimator;
import com.github.shareme.greenandroid.viewanim.fading_exits.FadeOutLeftAnimator;
import com.github.shareme.greenandroid.viewanim.fading_exits.FadeOutRightAnimator;
import com.github.shareme.greenandroid.viewanim.fading_exits.FadeOutUpAnimator;
import com.github.shareme.greenandroid.viewanim.flippers.FlipInXAnimator;
import com.github.shareme.greenandroid.viewanim.flippers.FlipOutXAnimator;
import com.github.shareme.greenandroid.viewanim.flippers.FlipOutYAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_entrances.RotateInAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_entrances.RotateInDownLeftAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_entrances.RotateInDownRightAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_entrances.RotateInUpLeftAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_entrances.RotateInUpRightAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_exits.RotateOutAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_exits.RotateOutDownLeftAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_exits.RotateOutDownRightAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_exits.RotateOutUpLeftAnimator;
import com.github.shareme.greenandroid.viewanim.rotating_exits.RotateOutUpRightAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideInDownAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideInLeftAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideInRightAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideInUpAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideOutDownAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideOutLeftAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideOutRightAnimator;
import com.github.shareme.greenandroid.viewanim.sliders.SlideOutUpAnimator;
import com.github.shareme.greenandroid.viewanim.specials.HingeAnimator;
import com.github.shareme.greenandroid.viewanim.specials.RollInAnimator;
import com.github.shareme.greenandroid.viewanim.specials.RollOutAnimator;
import com.github.shareme.greenandroid.viewanim.specials.in.DropOutAnimator;
import com.github.shareme.greenandroid.viewanim.specials.in.LandingAnimator;
import com.github.shareme.greenandroid.viewanim.specials.out.TakingOffAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_entrances.ZoomInAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_entrances.ZoomInDownAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_entrances.ZoomInLeftAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_entrances.ZoomInRightAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_entrances.ZoomInUpAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_exits.ZoomOutAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_exits.ZoomOutDownAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_exits.ZoomOutLeftAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_exits.ZoomOutRightAnimator;
import com.github.shareme.greenandroid.viewanim.zooming_exits.ZoomOutUpAnimator;

@SuppressWarnings("unused")
public enum Techniques {

    DropOut(DropOutAnimator.class),
    Landing(LandingAnimator.class),
    TakingOff(TakingOffAnimator.class),

    Flash(FlashAnimator.class),
    Pulse(PulseAnimator.class),
    RubberBand(RubberBandAnimator.class),
    Shake(ShakeAnimator.class),
    Swing(SwingAnimator.class),
    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
    StandUp(StandUpAnimator.class),
    Wave(WaveAnimator.class),

    Hinge(HingeAnimator.class),
    RollIn(RollInAnimator.class),
    RollOut(RollOutAnimator.class),

    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),

    FadeIn(FadeInAnimator.class),
    FadeInUp(FadeInUpAnimator.class),
    FadeInDown(FadeInDownAnimator.class),
    FadeInLeft(FadeInLeftAnimator.class),
    FadeInRight(FadeInRightAnimator.class),

    FadeOut(FadeOutAnimator.class),
    FadeOutDown(FadeOutDownAnimator.class),
    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    FadeOutUp(FadeOutUpAnimator.class),

    FlipInX(FlipInXAnimator.class),
    FlipOutX(FlipOutXAnimator.class),

    FlipOutY(FlipOutYAnimator.class),
    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
    RotateInDownRight(RotateInDownRightAnimator.class),
    RotateInUpLeft(RotateInUpLeftAnimator.class),
    RotateInUpRight(RotateInUpRightAnimator.class),

    RotateOut(RotateOutAnimator.class),
    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
    RotateOutDownRight(RotateOutDownRightAnimator.class),
    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),

    SlideInLeft(SlideInLeftAnimator.class),
    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
    SlideInDown(SlideInDownAnimator.class),

    SlideOutLeft(SlideOutLeftAnimator.class),
    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
    SlideOutDown(SlideOutDownAnimator.class),

    ZoomIn(ZoomInAnimator.class),
    ZoomInDown(ZoomInDownAnimator.class),
    ZoomInLeft(ZoomInLeftAnimator.class),
    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),

    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
    ZoomOutLeft(ZoomOutLeftAnimator.class),
    ZoomOutRight(ZoomOutRightAnimator.class),
    ZoomOutUp(ZoomOutUpAnimator.class);



    private Class animatorClazz;

    Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
