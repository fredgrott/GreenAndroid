package com.github.shareme.greenandroid.stateprogressbarapp.not_stateprogressbar.utils;


import com.github.shareme.greenandroid.stateprogressbarapp.AllStatesCompletedActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.AnimationFourStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.BasicFourStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.BasicThreeStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.BasicTwoStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.ChangingStatesSizeActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.CheckFourStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.ColoringStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.DescriptionFourStatesActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.StatesListActivity;
import com.github.shareme.greenandroid.stateprogressbarapp.UsageDetailsActivity;

/**
 * Created by Kofi Gyan on 7/15/2016.
 */

public class Utils {

    public static Class[] basicActivities = {BasicFourStatesActivity.class, BasicThreeStatesActivity.class, BasicTwoStatesActivity.class};
    public static Class[] allActivities = {StatesListActivity.class, CheckFourStatesActivity.class, AllStatesCompletedActivity.class, AnimationFourStatesActivity.class, DescriptionFourStatesActivity.class, ChangingStatesSizeActivity.class, ColoringStatesActivity.class, UsageDetailsActivity.class};


    public static Class selectActivity(int position, Class[] activities) {
        Class activity = activities[position];
        return activity;
    }


}
