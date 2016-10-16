/*
 Copyright 2015 florent37, Inc.
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
package com.github.shareme.greenandroid.materialleanback.cell;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.shareme.greenandroid.materialleanback.MaterialLeanBack;
import com.github.shareme.greenandroid.materialleanback.MaterialLeanBackSettings;
import com.github.shareme.greenandroid.materialleanback.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 28/08/15.
 */
@SuppressWarnings("unused")
public class CellViewHolder extends RecyclerView.ViewHolder {

    final static float scaleEnlarged = 1.2f;
    final static float scaleReduced = 1.0f;

    protected CardView cardView;
    protected boolean enlarged = false;

    protected final MaterialLeanBack.Adapter adapter;
    protected final MaterialLeanBack.ViewHolder viewHolder;
    protected final MaterialLeanBackSettings settings;
    public final int row;

    Animator currentAnimator;

    public CellViewHolder(View itemView, int row, MaterialLeanBack.Adapter adapter, MaterialLeanBackSettings settings) {
        super(itemView);
        this.row = row;
        this.adapter = adapter;
        this.settings = settings;

        cardView = (CardView) itemView.findViewById(R.id.cardView);
        this.viewHolder = adapter.onCreateViewHolder(cardView,row);
        this.viewHolder.row = row;
        cardView.addView(viewHolder.itemView);
    }

    public void enlarge(boolean withAnimation) {
        if (!enlarged && settings.animateCards) {

            if(currentAnimator != null) {
                currentAnimator.cancel();
                currentAnimator = null;
            }

            int duration = withAnimation? 300 : 0;

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(duration);

            List<Animator> animatorList = new ArrayList<>();
            animatorList.add(ObjectAnimator.ofFloat(cardView, "scaleX", scaleEnlarged));
            animatorList.add(ObjectAnimator.ofFloat(cardView, "scaleY", scaleEnlarged));

            if(settings.overlapCards) {
                //animatorList.add(ObjectAnimator.ofFloat(cardView, "translationX", translationX));
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        cardView.setCardElevation(settings.elevationEnlarged);
                        currentAnimator = null;
                    }
                });
            }

            animatorSet.playTogether(animatorList);
            currentAnimator = animatorSet;
            animatorSet.start();

            enlarged = true;
        }
    }

    public void reduce(boolean withAnimation) {
        if (enlarged && settings.animateCards) {
            if(currentAnimator != null) {
                currentAnimator.cancel();
                currentAnimator = null;
            }

            int duration = withAnimation? 300 : 0;

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(duration);

            List<Animator> animatorList = new ArrayList<>();
            animatorList.add(ObjectAnimator.ofFloat(cardView,"scaleX",scaleReduced));
            animatorList.add(ObjectAnimator.ofFloat(cardView,"scaleY",scaleReduced));

            if(settings.overlapCards) {
                //animatorList.add(ObjectAnimator.ofFloat(cardView, "translationX", translationX));
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        cardView.setCardElevation(settings.elevationReduced);
                        currentAnimator = null;
                    }
                });
            }

            animatorSet.playTogether(animatorList);
            currentAnimator = animatorSet;
            animatorSet.start();

            enlarged = false;
        }
    }

    public void newPosition(int position) {
        if (position == 1)
            enlarge(true);
        else
            reduce(true);
    }

    @SuppressWarnings("unchecked")
    public void onBind() {
        int cell = getAdapterPosition()-CellAdapter.PLACEHOLDER_START_SIZE;
        viewHolder.cell = cell;
        adapter.onBindViewHolder(viewHolder,cell);
    }

    public boolean isEnlarged() {
        return enlarged;
    }

    public void setEnlarged(boolean enlarged) {
        this.enlarged = enlarged;
    }
}
