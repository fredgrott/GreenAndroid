package com.github.shareme.greenandroid.spinkitapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.shareme.greenandroid.spinkit.SpinKitView;
import com.github.shareme.greenandroid.spinkit.sprite.Sprite;
import com.github.shareme.greenandroid.spinkit.style.ChasingDots;
import com.github.shareme.greenandroid.spinkit.style.Circle;
import com.github.shareme.greenandroid.spinkit.style.CubeGrid;
import com.github.shareme.greenandroid.spinkit.style.DoubleBounce;
import com.github.shareme.greenandroid.spinkit.style.FadingCircle;
import com.github.shareme.greenandroid.spinkit.style.FoldingCube;
import com.github.shareme.greenandroid.spinkit.style.Pulse;
import com.github.shareme.greenandroid.spinkit.style.RotatingPlane;
import com.github.shareme.greenandroid.spinkit.style.ThreeBounce;
import com.github.shareme.greenandroid.spinkit.style.WanderingCubes;
import com.github.shareme.greenandroid.spinkit.style.Wave;


public class DetailActivity extends AppCompatActivity implements Colors {

  public static void start(Context context, int position) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra("position", position);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(0);
    viewPager.setAdapter(new PagerAdapter() {
      String[] names = getResources().getStringArray(R.array.names);

      @Override
      public int getCount() {
        return 11;
      }

      @Override
      public boolean isViewFromObject(View view, Object object) {
        return view == object;
      }

      @Override
      public Object instantiateItem(ViewGroup container, int position) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager,
            null);
        final SpinKitView spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(names[position]);
        Sprite drawable = null;
        switch (position) {
          case 0:
            drawable = new RotatingPlane();
            break;
          case 1:
            drawable = new DoubleBounce();
            break;
          case 2:
            drawable = new Wave();
            break;
          case 3:
            drawable = new WanderingCubes();
            break;
          case 4:
            drawable = new Pulse();
            break;
          case 5:
            drawable = new ChasingDots();
            break;
          case 6:
            drawable = new ThreeBounce();
            break;
          case 7:
            drawable = new Circle();
            break;
          case 8:
            drawable = new CubeGrid();
            break;
          case 9:
            drawable = new FadingCircle();
            break;
          case 10:
            drawable = new FoldingCube();
            break;
        }
        spinKitView.setIndeterminateDrawable(drawable);
        container.addView(view);

        return view;
      }

      @Override
      public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
      }
    });
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int color = (int) ArgbEvaluator.getInstance().evaluate(positionOffset,
            colors[position % colors.length],
            colors[(position + 1) % colors.length]);
        getWindow().getDecorView().setBackgroundColor(color);
      }

      @Override
      public void onPageSelected(int position) {
        getWindow().getDecorView().setBackgroundColor(colors[position % colors.length]);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
  }
}
