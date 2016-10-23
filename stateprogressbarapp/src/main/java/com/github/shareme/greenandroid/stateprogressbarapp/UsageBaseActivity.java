package com.github.shareme.greenandroid.stateprogressbarapp;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.shareme.greenandroid.stateprogressbar.StateProgressBar;


/**
 * Created by Kofi Gyan on 7/17/2016.
 */

public abstract class UsageBaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected String[] descriptionData = {"Details", "Pricing", "Amenities", "Status"};
    protected Button nextBtn;
    protected Button backBtn;
    protected StateProgressBar stateprogressbar;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectCommonViews();
    }


    protected void injectCommonViews() {
        nextBtn = (Button) findViewById(R.id.btnNext);
        nextBtn.setOnClickListener(this);

        stateprogressbar = (StateProgressBar) findViewById(R.id.usage_stateprogressbar);
        stateprogressbar.setStateDescriptionData(descriptionData);
    }

    protected void injectBackView() {
        backBtn = (Button) findViewById(R.id.btnBack);
        backBtn.setOnClickListener(this);
    }


}
