package com.github.shareme.greenandroid.pathviewapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.shareme.greenandroid.pathview.PathView;


public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
		final ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		viewpager.setAdapter(screenSlidePagerAdapter);
		viewpager.setPageTransformer(true, new CustomTransformer());
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return new SettingsFragment();
			}
			if (position == 1) {
				return new IssuesFragment();
			}
			if (position == 2) {
				return new LogoutFragment();
			}
			return new SettingsFragment();
		}

		@Override
		public int getCount() {
			return 3;
		}
	}

	private class CustomTransformer implements ViewPager.PageTransformer {


		@Override
		public void transformPage(View page, float position) {
			if (position < -1 || position > 1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				page.setAlpha(0);
				return;
			}
			final float abs = 1 - Math.abs(position);
			final PathView pathView = (PathView) page.findViewById(R.id.pathView);
			pathView.setPercentage(abs);
		}
	}

}
