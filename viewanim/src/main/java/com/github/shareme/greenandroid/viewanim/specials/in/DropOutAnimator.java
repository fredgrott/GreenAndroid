package com.github.shareme.greenandroid.viewanim.specials.in;

import android.animation.ObjectAnimator;
import android.view.View;

import com.github.shareme.greenandroid.easing.Glider;
import com.github.shareme.greenandroid.easing.Skill;
import com.github.shareme.greenandroid.viewanim.BaseViewAnimator;

@SuppressWarnings("unused")
public class DropOutAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        int distance = target.getTop() + target.getHeight();
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                Glider.glide(Skill.BounceEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "translationY", -distance, 0))
        );
    }
}
