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
package com.github.shareme.greenandroid.materialleanback.line;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.shareme.greenandroid.materialleanback.MaterialLeanBack;
import com.github.shareme.greenandroid.materialleanback.MaterialLeanBackSettings;
import com.github.shareme.greenandroid.materialleanback.R;
import com.github.shareme.greenandroid.materialleanback.cell.CellAdapter;
import com.github.shareme.greenandroid.materialleanback.cell.CellViewHolder;


/**
 * Created by florentchampigny on 28/08/15.
 */
@SuppressWarnings("unused")
public class LineViewHolder extends RecyclerView.ViewHolder {

    protected final MaterialLeanBackSettings settings;
    protected final RecyclerView recyclerView;
    protected final MaterialLeanBack.Adapter adapter;
    protected final MaterialLeanBack.Customizer customizer;

    protected ViewGroup layout;
    protected TextView title;

    protected int row;
    protected boolean wrapped = false;

    public LineViewHolder(View itemView, @NonNull MaterialLeanBack.Adapter adapter, @NonNull MaterialLeanBackSettings settings, final MaterialLeanBack.Customizer customizer) {
        super(itemView);
        this.adapter = adapter;
        this.settings = settings;
        this.customizer = customizer;

        layout = (ViewGroup) itemView.findViewById(R.id.row_layout);
        title = (TextView) itemView.findViewById(R.id.row_title);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.row_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void onBind(int row) {
        this.row = row;

        {
            String titleString = adapter.getTitleForRow(this.row);
            if (titleString == null || titleString.trim().isEmpty())
                title.setVisibility(View.GONE);
            else
                title.setText(titleString);

            if (settings.titleColor != null)
                title.setTextColor(settings.titleColor);
            if (settings.titleSize != -1)
                title.setTextSize(settings.titleSize);

            if (this.customizer != null)
                customizer.customizeTitle(title);
        }
        {
            if (settings.lineSpacing != null) {
                layout.setPadding(
                        layout.getPaddingLeft(),
                        layout.getPaddingTop(),
                        layout.getPaddingRight(),
                        settings.lineSpacing
                );
            }
        }

        recyclerView.setAdapter(new CellAdapter(row, adapter, settings, new CellAdapter.HeightCalculatedCallback() {
            @Override
            public void onHeightCalculated(int height) {
                if(!wrapped) {
                    recyclerView.getLayoutParams().height = height;
                    recyclerView.requestLayout();
                    wrapped = true;
                }
            }
        }));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if (viewHolder instanceof CellViewHolder) {
                        CellViewHolder cellViewHolder = ((CellViewHolder) viewHolder);
                        cellViewHolder.newPosition(i);
                    }
                }
            }
        });
    }
}