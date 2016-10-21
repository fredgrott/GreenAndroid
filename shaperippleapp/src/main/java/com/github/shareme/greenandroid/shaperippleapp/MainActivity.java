package com.github.shareme.greenandroid.shaperippleapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.shareme.greenandroid.discreteseekbar.DiscreteSeekBar;
import com.github.shareme.greenandroid.shaperipple.ShapeRipple;
import com.github.shareme.greenandroid.shaperipple.model.Circle;
import com.github.shareme.greenandroid.shaperipple.model.Square;
import com.github.shareme.greenandroid.shaperipple.model.Star;
import com.github.shareme.greenandroid.shaperipple.model.Triangle;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, DiscreteSeekBar.OnProgressChangeListener {

    private ShapeRipple ripple;

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
        setupToolbar();
    }

    public void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        ((AppCompatCheckBox) findViewById(R.id.enable_color_transition)).setOnCheckedChangeListener(this);
        ((AppCompatCheckBox) findViewById(R.id.enable_single_ripple)).setOnCheckedChangeListener(this);
        ((AppCompatCheckBox) findViewById(R.id.enable_random_position)).setOnCheckedChangeListener(this);
        ((AppCompatCheckBox) findViewById(R.id.enable_random_color)).setOnCheckedChangeListener(this);

        ripple = (ShapeRipple) findViewById(R.id.ripple);
        ripple.setRippleShape(new Circle());
        DiscreteSeekBar rippleDuration = (DiscreteSeekBar) findViewById(R.id.ripple_duration);
        final DiscreteSeekBar rippleIntervalDuration = (DiscreteSeekBar) findViewById(R.id.ripple_interval);
        rippleDuration.setOnProgressChangeListener(this);
        rippleIntervalDuration.setOnProgressChangeListener(this);

        rippleIntervalDuration.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return 0;
            }

            @Override
            public String transformToString(int value) {
                return String.format("%.2f", (float) value / 100F);
            }

            @Override
            public boolean useStringTransform() {
                return true;
            }
        });
    }

    private void setupToolbar() {
        AppCompatActivity activity = this;
        activity.setSupportActionBar(mToolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.ripples, R.string.ripple_circle) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        navigationView.setCheckedItem(R.id.nav_circle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_circle:
                        ripple.setRippleShape(new Circle());
                        break;
                    case R.id.nav_square:
                        ripple.setRippleShape(new Square());
                        break;
                    case R.id.nav_triangle:
                        ripple.setRippleShape(new Triangle());
                        break;
                    case R.id.nav_star:
                        ripple.setRippleShape(new Star());
                        break;
                    case R.id.nav_github:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/poldz123/ShapeRipple"));
                        startActivity(browserIntent);
                        break;
                    case R.id.nav_about:

                        @SuppressLint("InflateParams") View layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.about_dialog, null, false);
                        TextView version = (TextView) layout.findViewById(R.id.version);

                        version.setText(String.format("Version %s", MainActivity.this.getString(R.string.version)));
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setPositiveButton("Ok", null);
                        builder.setView(layout);

                        final AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        dialog.show();

                        break;

                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        switch (id) {
            case R.id.enable_color_transition:
                ripple.setEnableColorTransition(buttonView.isChecked());
                break;
            case R.id.enable_single_ripple:
                ripple.setEnableSingleRipple(buttonView.isChecked());
                break;
            case R.id.enable_random_position:
                ripple.setEnableRandomPosition(buttonView.isChecked());
                break;
            case R.id.enable_random_color:
                ripple.setEnableRandomColor(buttonView.isChecked());
                break;
            default:

        }
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.ripple_duration:
                ripple.setRippleDuration(seekBar.getProgress());
                break;
            case R.id.ripple_interval:
                ripple.setRippleInterval((float) seekBar.getProgress() / 100F);
                break;
            default:

        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
    }

    public void colorPick(View view) {
        ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();

        ripple.setRippleColor(colorDrawable.getColor());
        ripple.setRippleFromColor(colorDrawable.getColor());
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
