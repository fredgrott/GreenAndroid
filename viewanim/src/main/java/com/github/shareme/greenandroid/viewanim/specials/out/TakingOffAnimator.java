package com.github.shareme.greenandroid.viewanim.specials.out;

import android.animation.ObjectAnimator;
import android.view.View;

import com.github.shareme.greenandroid.easing.Glider;
import com.github.shareme.greenandroid.easing.Skill;
import com.github.shareme.greenandroid.viewanim.BaseViewAnimator;

@SuppressWarnings("unused")
public class TakingOffAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleX", 1f, 1.5f)),
                Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleY", 1f, 1.5f)),
                Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "alpha", 1, 0))
        );
    }
}
