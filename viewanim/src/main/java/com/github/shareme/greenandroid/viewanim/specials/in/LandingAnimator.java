package com.github.shareme.greenandroid.viewanim.specials.in;

import android.animation.ObjectAnimator;
import android.view.View;

import com.github.shareme.greenandroid.easing.Glider;
import com.github.shareme.greenandroid.easing.Skill;
import com.github.shareme.greenandroid.viewanim.BaseViewAnimator;

@SuppressWarnings("unused")
public class LandingAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleX", 1.5f, 1f)),
                Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleY", 1.5f, 1f)),
                Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "alpha", 0, 1f))
        );
    }
}
