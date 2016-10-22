package com.github.shareme.greenandroid.spinkitapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * Created by ybq.
 */
public class Page1Fragment extends Fragment implements Colors {


    private RecyclerView recyclerView;


    public static Page1Fragment newInstance() {
        return new Page1Fragment();
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page1, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setBackgroundColor(colors[4]);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
                return new Holder(view);
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                holder.bind(position % 11);
            }

            @Override
            public int getItemCount() {
                return 11;
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder {


        SpinKitView spinKitView;

        public Holder(View itemView) {
            super(itemView);
            spinKitView = (SpinKitView) itemView.findViewById(R.id.spin_kit);
        }

        public void bind(final int position) {
            itemView.setBackgroundColor(colors[position % colors.length]);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailActivity.start(v.getContext(), position);
                }
            });
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
        }
    }
}
