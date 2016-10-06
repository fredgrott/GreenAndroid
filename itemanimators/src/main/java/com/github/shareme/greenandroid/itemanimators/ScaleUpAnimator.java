/*
 Copyright 2016 Mike Penz
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
package com.github.shareme.greenandroid.itemanimators;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mikepenz on 08.01.16.
 */
@SuppressWarnings("unused")
public class ScaleUpAnimator extends BaseScaleAnimator<ScaleUpAnimator> {
    public void addAnimationPrepare(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView, 0);
        ViewCompat.setScaleY(holder.itemView, 0);
    }

    public ViewPropertyAnimatorCompat addAnimation(RecyclerView.ViewHolder holder) {
        return ViewCompat.animate(holder.itemView).scaleX(1).scaleY(1).setDuration(getAddDuration()).setInterpolator(getInterpolator());
    }

    public void addAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView, 1);
        ViewCompat.setScaleY(holder.itemView, 1);
    }


    public ViewPropertyAnimatorCompat removeAnimation(RecyclerView.ViewHolder holder) {
        return ViewCompat.animate(holder.itemView).setDuration(getRemoveDuration()).scaleX(0).scaleY(0).setInterpolator(getInterpolator());
    }

    public void removeAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView, 1);
        ViewCompat.setScaleY(holder.itemView, 1);
    }

    public float changeAnimationPrepare1(RecyclerView.ViewHolder holder) {
        return ViewCompat.getScaleX(holder.itemView);
    }

    public void changeAnimationPrepare2(RecyclerView.ViewHolder holder, float prevValue) {
        ViewCompat.setScaleX(holder.itemView, prevValue);
    }

    public void changeAnimationPrepare3(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView, 0);
        ViewCompat.setScaleY(holder.itemView, 0);
    }

    public ViewPropertyAnimatorCompat changeOldAnimation(RecyclerView.ViewHolder holder, ChangeInfo changeInfo) {
        return ViewCompat.animate(holder.itemView).setDuration(getChangeDuration()).scaleX(0).scaleY(0).translationX(changeInfo.toX - changeInfo.fromX).translationY(changeInfo.toY - changeInfo.fromY).setInterpolator(getInterpolator());
    }

    public ViewPropertyAnimatorCompat changeNewAnimation(RecyclerView.ViewHolder holder) {
        return ViewCompat.animate(holder.itemView).translationX(0).translationY(0).setDuration(getChangeDuration()).scaleX(1).scaleY(1).setInterpolator(getInterpolator());
    }

    public void changeAnimationCleanup(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView, 1);
        ViewCompat.setScaleY(holder.itemView, 1);
    }
}
