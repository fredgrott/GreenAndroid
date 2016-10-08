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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.shareme.greenandroid.materialleanback.MaterialLeanBack;
import com.github.shareme.greenandroid.materialleanback.MaterialLeanBackSettings;
import com.github.shareme.greenandroid.materialleanback.PlaceHolderViewHolder;
import com.github.shareme.greenandroid.materialleanback.R;


/**
 * Created by florentchampigny on 31/08/15.
 */
public class LineAdapter extends RecyclerView.Adapter {

    public static final int PLACEHOLDER_START = -2000;
    public static final int PLACEHOLDER_START_SIZE = 1;
    public static final int PLACEHOLDER_END_SIZE = 1;
    public static final int PLACEHOLDER_END = -2001;
    public static final int CELL = -2002;

    protected MaterialLeanBackSettings settings;
    protected MaterialLeanBack.Adapter adapter;
    protected MaterialLeanBack.Customizer customizer;

    public LineAdapter(@NonNull MaterialLeanBackSettings settings, @NonNull MaterialLeanBack.Adapter adapter, MaterialLeanBack.Customizer customizer) {
        this.settings = settings;
        this.adapter = adapter;
        this.customizer = customizer;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return PLACEHOLDER_START;
        else if (position == getItemCount() - 1)
            return PLACEHOLDER_END;
        else if (adapter != null && adapter.isCustomView(position - 1))
            return position-1;
        else
            return CELL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view;
        switch (type) {
            case PLACEHOLDER_START:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mlb_placeholder, viewGroup, false);
                return new PlaceHolderViewHolder(view, false, settings.paddingTop);
            case PLACEHOLDER_END:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mlb_placeholder, viewGroup, false);
                return new PlaceHolderViewHolder(view, false, settings.paddingBottom);
            case CELL:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mlb_row, viewGroup, false);
                return new LineViewHolder(view, adapter, settings, customizer);
            default:
                if(adapter != null && adapter.isCustomView(type)){
                    return adapter.getCustomViewForRow(viewGroup,type);
                }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int row) {
        if (viewHolder instanceof LineViewHolder)
            ((LineViewHolder) viewHolder).onBind(row-PLACEHOLDER_START_SIZE);
        else if(adapter != null && adapter.isCustomView(row-PLACEHOLDER_START_SIZE))
            adapter.onBindCustomView(viewHolder,row-PLACEHOLDER_START_SIZE);

    }

    @Override
    public int getItemCount() {
        return adapter.getLineCount()+PLACEHOLDER_START_SIZE+PLACEHOLDER_END_SIZE;
    }
}
