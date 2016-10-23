package com.github.shareme.greenandroid.transitionseverywhereapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.shareme.greenandroid.transitionseverywhere.ArcMotion;
import com.github.shareme.greenandroid.transitionseverywhere.ChangeBounds;
import com.github.shareme.greenandroid.transitionseverywhere.TransitionManager;


/**
 * Created by Andrey Kulikov on 24/03/16.
 */
public class PathMotionSample extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_path, container, false);

        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
        final View button = transitionsContainer.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            boolean mToRightAnimation;

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer,
                    new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));

                mToRightAnimation = !mToRightAnimation;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
                params.gravity = mToRightAnimation ? (Gravity.RIGHT | Gravity.BOTTOM) :
                    (Gravity.LEFT | Gravity.TOP);
                button.setLayoutParams(params);
            }

        });

        return view;
    }


}
