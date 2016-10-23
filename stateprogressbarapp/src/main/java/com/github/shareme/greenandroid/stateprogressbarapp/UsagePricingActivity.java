package com.github.shareme.greenandroid.stateprogressbarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.shareme.greenandroid.stateprogressbar.StateProgressBar;


/**
 * Created by Kofi Gyan on 7/13/2016.
 */

public class UsagePricingActivity extends UsageBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usage_pricing);

        injectBackView();

        stateprogressbar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNext:
                Intent intent = new Intent(getApplicationContext(), UsageAmenitiesActivity.class);
                startActivity(intent);
                break;

            case R.id.btnBack:
                finish();
                break;
        }
    }

}
