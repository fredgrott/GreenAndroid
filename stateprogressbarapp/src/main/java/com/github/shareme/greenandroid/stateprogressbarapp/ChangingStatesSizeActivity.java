package com.github.shareme.greenandroid.stateprogressbarapp;

import android.app.Activity;
import android.os.Bundle;

import com.github.shareme.greenandroid.stateprogressbar.StateProgressBar;


/**
 * Created by Kofi Gyan on 7/22/2016.
 */
public class ChangingStatesSizeActivity extends Activity{

    String[] descriptionData = {"Details" , "Status" , "Photo" , "Event" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_states_size);
        StateProgressBar stateProgressBar = (StateProgressBar)findViewById(R.id.state_progress_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);
    }
}
